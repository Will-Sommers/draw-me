(ns draw-me.components.canvas
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.mouse-state :as mouse-state]
            [draw-me.utils :as utils]))

(enable-console-print!)

(defn record-mouse [data owner event]
  (let [event (assoc event :timestamp (- (:timestamp event)
                                         (:initial-time @data)))
        event (assoc event :color (get-in @data [:palette :color]))]
    ;; Bad, will!
    (reset! mouse-state/mouse-state {:mouse-position {:x-pos (:x-pos event)  :y-pos (:y-pos event)}})
    (when (om/get-state owner :record-mouse)
      (om/transact! data :in-progress-line #(conj % event)))))

(defn reset-mouse-positions [data]
  (om/transact! data :complete-lines #(assoc % (symbol (count %))
                                             {:mouse-positions (:in-progress-line @data)
                                              :selected true}))
    (om/update! data :in-progress-line []))

(defn drawing-time-bound [position current-millisecond total-milliseconds tail-lifetime]
  (let [tail-lifetimes 500
        time-floor (- current-millisecond tail-lifetime)]
    (if (< time-floor 0)
      (let [real-time-floor (+ total-milliseconds time-floor)]
        (or (> current-millisecond (mod (:timestamp position) total-milliseconds))
            (> total-milliseconds (mod (:timestamp position) total-milliseconds) real-time-floor))
        )
      (> current-millisecond (mod (:timestamp position) total-milliseconds) time-floor))))

(defn draw-canvas [data owner]
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
                             [(utils/listen (om/get-node owner "draw-loop-ref") "mousemove")])
            mouse-up-chan (async/map
                           (fn [e] (utils/event->hash e))
                           [(utils/listen (om/get-node owner "draw-loop-ref") "mouseup")])
            ]
        (go (while true
                (alt!
                 mouse-move-chan ([pos] (record-mouse data owner pos))
                 mouse-up-chan ([pos] (do
                                        (om/set-state! owner :record-mouse false)
                                        (reset-mouse-positions data)
                                        )))))))

    om/IDidUpdate
    (did-update [_ _ _]
      (om/set-state! owner :last-millisecond (utils/timestamp))

      (let [current-millisecond (utils/time->delta data)
            total-milliseconds (get-in data [:time-loop :total-milliseconds])
            canvas (om/get-node owner (str (get-in data [:canvas :name]) "-ref"))
            selected-lines (filter #(:selected (val %)) (:complete-lines data))
            currently-drawing-pos (filter #(drawing-time-bound % current-millisecond total-milliseconds (get-in data [:time-loop :tail-in-milliseconds])) (:in-progress-line data))]
        
        (utils/clear-canvas! canvas (get-in data [:canvas :width]) (get-in data [:canvas :height]) 400)
        (if (not (empty? selected-lines))
          (doseq [completed-line (vals selected-lines)]
            (println completed-line)
            (let [draw-positions (filter #(drawing-time-bound % current-millisecond total-milliseconds(get-in data [:time-loop :tail-in-milliseconds])) (:mouse-positions completed-line))]
              (if (:hover completed-line)
                (utils/slope-draw! draw-positions canvas "#067300")
                (utils/slope-draw! draw-positions canvas (:color (first draw-positions))))
              (utils/slope-draw! currently-drawing-pos canvas)))
          (utils/slope-draw! currently-drawing-pos canvas))))
    
    om/IRender
    (render [_]
      (dom/div nil
               (dom/canvas #js {:id (get-in data [:canvas :name])
                                :height (get-in data [:canvas :height])
                                :width (get-in data [:canvas :width])
                                :style #js {:border  "1px solid black"}
                                :ref (str (get-in data [:canvas :name]) "-ref")
                                :onMouseDown #(om/set-state! owner :record-mouse true)})))))
