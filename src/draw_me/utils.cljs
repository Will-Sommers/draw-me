(ns draw-me.utils
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [goog.events :as events]
            [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]])
  (:import [goog.events EventType]))

(defn slope [x y x1 y1]
  (/ (- y1 y)
     (- x1 x)))

(defn timestamp []
  (.getTime (new js/Date)))

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type (fn [event]
                             (.preventDefault event)
                             (put! out (hash-map :event event :timestamp (timestamp)))))
    out))

(defn point-draw! [point canvas]
  (canvas-draw! (:color point) canvas (:x-pos point) (:y-pos point) 2 2))

(defn canvas-draw! [color canvas x y x-length y-length]
  (let [context (. canvas (getContext "2d"))]
    (set! (.-fillStyle context) color)
    (.fillRect context x y x-length y-length)))

(defn draw-lines! [color data owner sym-name dom-node-ref]
  (let [canvas dom-node-ref]
    (doseq [x (->> (sym-name data)
                   last
                   (:mouse-positions))]
      (canvas-draw! color canvas (:x-pos x) (:y-pos x) 2 2))))

(defn clear-canvas! [canvas w h]
  (let [context (. canvas (getContext "2d"))]
    (set! (.-fillStyle context) "#000000")
    (.clearRect context 0 0 w h)))

(defn time->delta [data]
  (let [current-time (:current-millisecond data)
        initial-time (:initial-time data)
        total-milliseconds (get-in data [:time-loop :total-milliseconds])]
      (mod (- current-time initial-time)
           total-milliseconds)))

