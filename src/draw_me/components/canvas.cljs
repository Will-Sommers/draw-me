(ns draw-me.components.canvas
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
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
  (let [hash-pos (inc (:line-count @data))]
    (om/set-state! owner :record-mouse false)
    (om/transact! data :complete-lines #(assoc % (symbol hash-pos)
                                               {:mouse-positions (:in-progress-line @data)
                                                :hash hash-pos
                                                :selected true}))
    (om/update! data :line-count hash-pos)
    (om/update! data :in-progress-line [])))

(defn time->delta [data]
  (let [current-time (- (:current-millisecond data) (get-in data [:time-loop :cummulative-pause-time]))
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

(defn draw-canvas [data owner]
  (let [canvas-name (get-in data [:canvas :name])
        canvas-width (get-in data [:canvas :width])
        canvas-height (get-in data [:canvas :height])]
    (reify
      
      om/IInitState
      (init-state [_]
        {:c-mouse (chan)
         :mouse-positions []})
      
      om/IDidMount
      (did-mount [_]
        (let [c-mouse (om/get-state owner :c-mouse)
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

      om/IDidUpdate
      (did-update [_ _ _]
        (if (nil? (get-in data [:time-loop :pause-start]))
          (do
            (om/set-state! owner :last-millisecond (utils/timestamp))
            
            (let [current-millisecond (time->delta data)
                  loop-length (get-in data [:time-loop :loop-in-milliseconds])
                  tail-length (get-in data [:time-loop :tail-in-milliseconds])
                  canvas (om/get-node owner canvas-name)
                  selected-lines (filter #(:selected (val %)) (:complete-lines data))]
              (utils/clear-canvas! canvas canvas-width canvas-height)

              (if-not (empty? (:in-progress-line data))
                (let [currently-drawing-line (filter
                                              #(drawing-time-bound % current-millisecond loop-length tail-length)
                                              (:in-progress-line data))]
                  (utils/slope-draw! currently-drawing-line canvas "black")))

              
              (doseq [completed-line (vals selected-lines)]
                (let [draw-positions (filter
                                      #(drawing-time-bound % current-millisecond loop-length tail-length)
                                      (:mouse-positions completed-line))]
                  (if (:hover completed-line)
                    (utils/slope-draw! draw-positions canvas (get-in data [:palette :highlight-color]))
                    (utils/slope-draw! draw-positions canvas (:color (first draw-positions))))))))))
      
      om/IRender
      (render [_]
        (dom/div nil
                 (dom/canvas #js {:id canvas-name
                                  :height canvas-height
                                  :width canvas-width
                                  :style #js {:border  "1px solid black"}
                                  :ref canvas-name
                                  :onMouseDown #(om/set-state! owner :record-mouse true)})))))
)
