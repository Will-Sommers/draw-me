(ns draw-me.components.canvas
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [draw-me.mouse-state :as mouse-state]
            [draw-me.utils :as utils]))

(enable-console-print!)

(defn record-mouse [data owner event]
  (let [event (assoc event :timestamp (mod (- (:timestamp event)
                                              (:initial-time @data))
                                           (get-in @data [:time-loop :loop-in-milliseconds])))
        event (assoc event :color (get-in @data [:palette :color]))]
    ;; Bad, will!
    (reset! mouse-state/mouse-state {:mouse-position {:x-pos (:x-pos event)
                                                      :y-pos (:y-pos event)}})
    (when (om/get-state owner :record-mouse)
      (om/transact! data :in-progress-line #(conj % event)))))

(defn reset-mouse-positions [data owner]
  (when (om/get-state owner :record-mouse)
    (let [hash-pos (inc (:line-count @data))]
      (om/set-state! owner :record-mouse false)
      (om/transact! data :complete-lines #(assoc % (symbol hash-pos)
                                                 {:mouse-positions (:in-progress-line @data)
                                                  :hash hash-pos
                                                  :selected true}))
      (om/update! data :line-count hash-pos)
      (om/update! data :in-progress-line []))))

(defn time->delta [data current-millisecond]
  (let [current-time (- current-millisecond (get-in data [:time-loop :cummulative-pause-time]))
        initial-time (:initial-time data)
        total-milliseconds (get-in data [:time-loop :loop-in-milliseconds])]
      (mod (- current-time initial-time)
           total-milliseconds)))

(defn drawing-time-bound [position current-millisecond loop-length tail-lifetime]
  (let [draw-bound-time (- current-millisecond tail-lifetime)
        timestamp (:timestamp position)]
    (if (neg? draw-bound-time)
      (let [draw-bound-time (+ loop-length draw-bound-time)]
        (or (> current-millisecond timestamp)
            (> loop-length timestamp draw-bound-time)))
      (> current-millisecond timestamp  draw-bound-time))))

(defn get-draw-positions [data current-millisecond map-key line-data]
  (let [loop-length (get-in data [:time-loop :loop-in-milliseconds])
        tail-length (get-in data [:time-loop :tail-in-milliseconds])]
    (filter
      #(drawing-time-bound % current-millisecond loop-length tail-length)
      (map-key line-data))))

(defn tick-draw-loop [data owner]
  (let [current-millisecond (time->delta data (om/get-state owner :current-millisecond))
        selected-lines (filter #(:selected (val %)) (:complete-lines data))
        canvas-name (get-in data [:canvas :name])
        context (->> canvas-name
                  (om/get-node owner)
                  (#(. % (getContext "2d"))))]

    (om/set-state! owner :last-millisecond (utils/timestamp))
    (utils/clear-canvas! context (get-in data [:canvas :width]) (get-in data [:canvas :height]))

    (if-not (empty? (:in-progress-line data))
      (let [currently-drawing-line (get-draw-positions data current-millisecond :in-progress-line data)]
        (utils/slope-draw! currently-drawing-line context "black")))

    (doseq [completed-line (vals selected-lines)]
      (let [draw-positions (get-draw-positions data current-millisecond :mouse-positions completed-line)
            color (if (:hover completed-line)
                    (get-in data [:palette :highlight-color])
                    "black")]
        (utils/slope-draw! draw-positions context color)))))


(defcomponent draw-canvas [data owner]

  (display-name [_]
    "Canvas")

  (init-state [_]
    {:c-mouse (chan)
     :mouse-positions []})

  (did-mount [_]
    (let [c-mouse (om/get-state owner :c-mouse)
          canvas-name (get-in data [:canvas :name])
          mouse-move-chan (async/map
                            (fn [e] (utils/event->hash e))
                            [(utils/listen (om/get-node owner canvas-name) "mousemove")])
          mouse-up-chan (async/map
                          (fn [e] (utils/event->hash e))
                          [(utils/listen (om/get-node owner canvas-name) "mouseup")])]
      (go (while true
            (alt!
              mouse-move-chan ([pos] (record-mouse data owner pos))
              mouse-up-chan ([pos] (reset-mouse-positions data owner)))))))

  (did-update [_ _ _]
    (let [paused? (om/get-state owner :paused?)]
      (if-not paused?
        (tick-draw-loop data owner))))

  (render-state [_ {:keys [paused?]}]
    (dom/div
      (dom/canvas {:id (get-in data [:canvas :name])
                   :class "canvas"
                   :height (get-in data [:canvas :height])
                   :width (get-in data [:canvas :width])
                   :ref (get-in data [:canvas :name])
                   :on-mouse-down #(if paused?
                                     (utils/log "nope")
                                     (om/set-state! owner :record-mouse true))}))))
