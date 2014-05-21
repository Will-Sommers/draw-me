(ns draw-me.components.playhead
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.utils :as utils]))

(defn draw-nav-time [data owner ref head-pos]
  (let [canvas (om/get-node owner ref)
        width (get-in data [:time-loop :width])
        height (get-in data [:time-loop :height])
        color "#000000"]    
    (utils/clear-canvas! canvas width height)
    (utils/canvas-draw! color canvas head-pos 0 2 height)))

(defn start-scrubber [data owner pos]
  (om/transact! data [:time-loop :pause-start] utils/timestamp))

(defn scrubber [data owner pos]
  (let [time-pos (* (/ (:x-pos pos)
                       (get-in @data [:time-loop :width]))
                    (get-in @data [:time-loop :loop-in-milliseconds]))]
    (println time-pos)
    (om/transact! data :current-millisecond (fn [] time-pos))))

(defn time-loop [data owner]
  (reify

    om/IDidMount
    (did-mount [_]
      (let [mouse-down-chan (async/map
                             (fn [e]
                               (utils/event->hash e))
                             [(utils/listen (om/get-node owner "time-loop-ref") "mousedown")])
            mouse-move-chan (async/map
                             (fn [e] (utils/event->hash e))
                             [(utils/listen (om/get-node owner "time-loop-ref") "mousemove")])
            mouse-up-chan (async/map
                           (fn [e] (utils/event->hash e))
                           [(utils/listen (om/get-node owner "time-loop-ref") "mouseup")])]
          (go (while true
                (alt!
                 mouse-down-chan ([pos] (start-scrubber data owner pos))
                 mouse-move-chan ([pos] (scrubber data owner pos))
                 mouse-up-chan ([pos] (om/set-state! owner :record-mouse false)))))))
    
    om/IDidUpdate
    (did-update [_ _ _]
      (let [width (get-in data [:time-loop :width])
            loop-length (get-in data [:time-loop :loop-in-milliseconds])
            frame-px-width (/ width loop-length)
            current-millisecond (om/get-state owner :current-millisecond)
            adjusted-current-millisecond (- current-millisecond (get-in data [:time-loop :cummulative-pause-time]))
            current-time (mod (- adjusted-current-millisecond (:initial-time data))
                              loop-length)
            head-px-position (* frame-px-width current-time)
            ]
        (if (nil? (get-in data [:time-loop :pause-start]))
          (draw-nav-time data owner "time-loop-ref" head-px-position))))

    om/IRender
    (render [_]
      (dom/div nil
               (dom/canvas #js {:id "time-loop"
                                :height (get-in data [:time-loop :height])
                                :width (get-in data [:time-loop :width])
                                :style #js {:border "1px solid black"}
                                :ref "time-loop-ref"})))))
