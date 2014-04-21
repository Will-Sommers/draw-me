(ns draw-me.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.components.history :as history]
            [draw-me.components.playhead :as playhead]
            [draw-me.utils :as utils]))

(enable-console-print!)

(def app-state (atom {:text "Hell worlds!"
                      :complete-lines []
                      :in-progress-line []
                      :time-loop {:width 600
                                  :height 200
                                  :seconds 5}
                      :frame 0
                      :initial-time nil}))

(defn record-mouse [data owner event]
  (let [event (assoc event :timestamp (- (:timestamp event) (:initial-time @data)))]
      (when (om/get-state owner :record-mouse)
        (utils/canvas-draw (om/get-node owner "draw-loop-ref") (:x-pos event) (:y-pos event) 2 2)
        (println event)
        (om/transact! data :in-progress-line #(conj % event)))))

(defn reset-mouse-positions [data]
  (om/transact! data :complete-lines #(conj % {:mouse-positions (:in-progress-line @data)
                                               :index (inc (count %))}))
  (om/update! data :in-progress-line []))

(defn event->hash [e]
  (hash-map
   :timestamp (:timestamp e)
   :x-pos (.-clientX (:event e))
   :y-pos (.-clientY (:event e))))

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
                             (fn [e] (event->hash e))
                             [(utils/listen (. js/document (getElementById "draw-loop")) "mousemove")])
            mouse-up-chan (async/map
                           (fn [e] (event->hash e))
                           [(utils/listen (. js/document (getElementById "draw-loop")) "mouseup")])
            ]
        (go (while true
                (alt!
                 mouse-move-chan ([pos] (record-mouse data owner pos))
                 mouse-up-chan ([pos] (do
                                        (om/set-state! owner :record-mouse false)
                                        (reset-mouse-positions data)
                                        )))))))
    om/IRender
    (render [_]
      (dom/div nil
               (dom/canvas #js {:id "draw-loop"
                                :height 400
                                :width 400
                                :style #js {:border  "1px solid black"}
                                :ref "draw-loop-ref"
                                :onMouseDown #(do
                                                (om/set-state! owner :record-mouse true))})))))

(defn app [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:c-loop (chan)
       :time-pos 0})
    om/IDidMount
    (did-mount [_]
      (let [tick (fn tick []
                   (om/transact! data :frame inc)
                   (js/requestAnimationFrame tick))]        
        (om/transact! data :initial-time utils/timestamp)
        (tick)))
    om/IRender
    (render [_]
      (let [c-time (om/get-state owner :c-time)]
        (dom/div nil
                 (om/build draw-canvas data)
                 (om/build history/history-viewer (:complete-lines data))
                 (om/build playhead/time-loop data {:init-state {:c-time c-time}}))))))

(om/root
  app
  app-state
  {:target (. js/document (getElementById "app"))})


