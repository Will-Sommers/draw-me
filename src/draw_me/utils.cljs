(ns draw-me.utils
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [goog.events :as events]
            [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]])
  (:import [goog.events EventType]))

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type (fn [event]
                             (.preventDefault event)
                             (put! out (hash-map :event event :timestamp (.getTime (new js/Date))))))
    out))

(defn canvas-draw [canvas x y x-length y-length]
  (let [context (. canvas (getContext "2d"))]
    (set! (.-fillStyle context) "#000000")
    ;; TODO figure out the exact offset 
    (.fillRect context x y x-length y-length)))

(defn draw-lines [data owner sym-name dom-node-ref]
  (let [canvas dom-node-ref]
    (doseq [x (->> (sym-name data)
                   last
                   (:mouse-positions))]
      (canvas-draw canvas (:x-pos x) (:y-pos x) 2 2))))

(defn clear-canvas [canvas w h]
  (let [context (. canvas (getContext "2d"))]
    (.clearRect context 0 0 w h)))
