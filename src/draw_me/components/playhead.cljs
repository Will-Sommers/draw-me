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
    (utils/canvas-draw! color canvas head-pos 0 1 height)))


(defn draw-time-markers [context width height time-loop-length]
  (let [px-width (/ width time-loop-length)
        points (take-while (partial > time-loop-length) (map #(* 125 %) (iterate inc 1)))]
    (doseq [point points]
      (let [point-pos (* point px-width)
            tick (condp = (mod point 1000)
                   0 {:height 10 :second (/ point 1000)}
                   500 {:height 6}
                   {:height 4})]
        (do
          (if (:second tick)
            (do
              (set! (.-font context) "normal normal 12px sans-serif")
              (.fillText context (:second tick) (+ 5  point-pos) (- height 2))))
          (set! (.-fillStyle context) "black")
          (.fillRect context point-pos (- height (:height tick)) 1 height))))))

(defn start-scrubber [data owner pos]
  (om/transact! data [:time-loop :pause-start] utils/timestamp))
;
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
            height (get-in data [:time-loop :height])
            loop-length (get-in data [:time-loop :loop-in-milliseconds])
            frame-px-width (/ width loop-length)
            current-millisecond (om/get-state owner :current-millisecond)
            adjusted-current-millisecond (- current-millisecond (get-in data [:time-loop :cummulative-pause-time]))
            current-time (mod (- adjusted-current-millisecond (:initial-time data))
                              loop-length)
            head-px-position (* frame-px-width current-time)
            ]
        (if (nil? (get-in data [:time-loop :pause-start]))
          (do
            (let [context (. (om/get-node owner "time-loop-ref") (getContext "2d"))]
              (utils/clear-canvas! context width height)
              (draw-nav-time data owner "time-loop-ref" head-px-position)
              (draw-time-markers context width height loop-length)


              )))))

    om/IRender
    (render [_]
      (dom/div nil
               (dom/canvas #js {:id "time-loop"
                                :height (get-in data [:time-loop :height])
                                :width (get-in data [:time-loop :width])
                                :ref "time-loop-ref"})))))
