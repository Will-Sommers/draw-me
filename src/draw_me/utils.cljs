(ns draw-me.utils
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [goog.events :as events]
            [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]])
  (:import [goog.events EventType]))


(defn timestamp []
  (.getTime (new js/Date)))

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type (fn [event]
                             (.preventDefault event)
                             (put! out (hash-map :event event :timestamp (timestamp)))))
    out))

(defn slope-draw! [point-vec context color]
  (let [point-pairs (partition 2 1 point-vec)]
    (.beginPath context)
    (set! (.-strokeStyle context) color)
    (doseq [points point-pairs]
      (let [p0 (first points)
            p1 (second points)]
        (.bezierCurveTo context (:x-pos p0) (:y-pos p0) (:x-pos p1) (:y-pos p1) (:x-pos p0) (:y-pos p0))
        (.stroke context)))))

(defn canvas-draw! [color canvas x y x-length y-length]
  (let [context (. canvas (getContext "2d"))]
    (set! (.-fillStyle context) color)
    (.fillRect context x y x-length y-length)))

(defn point-draw! [point canvas]
  (canvas-draw! (:color point) canvas (:x-pos point) (:y-pos point) 2 2))

(defn draw-lines! [color data owner sym-name dom-node-ref]
  (let [canvas dom-node-ref]
    (doseq [x (->> (sym-name data)
                   last
                   (:mouse-positions))]
      (canvas-draw! color canvas (:x-pos x) (:y-pos x) 2 2))))

(defn clear-canvas! [context w h]
  (set! (.-fillStyle context) "#000000")
  (.clearRect context 0 0 w h))

(defn event->hash [e]
  (hash-map
   :timestamp (:timestamp e)
   :x-pos (.-offsetX (:event e))
   :y-pos (.-offsetY (:event e))))

(defn log [q]
  (.log js/console q))

(defn loge [e]
  (.persist e)
  (log e))
