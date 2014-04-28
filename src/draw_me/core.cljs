(ns draw-me.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.components.history :as history]
            [draw-me.components.playhead :as playhead]
            [draw-me.components.draggable :as draggable]
            [draw-me.utils :as utils]
            [draw-me.app-state :as app-state]
            [draw-me.mouse :as mouse]
            [ankha.core :as ankha]))

(enable-console-print!)

(defn record-mouse [data owner event]
  (let [event (assoc event :timestamp (- (:timestamp event)
                                         (:initial-time @data)))
        event (assoc event :color (get-in @data [:palette :color]))]
    (om/update! data :mouse-position {:x-pos (:x-pos event)  :y-pos (:y-pos event)})
    (when (om/get-state owner :record-mouse)
      (om/transact! data :in-progress-line #(conj % event)))))

(defn reset-mouse-positions [data]
  (om/transact! data :complete-lines #(conj % {:mouse-positions (:in-progress-line @data)
                                               :index (inc (count %))
                                               :selected true}))
    (om/update! data :in-progress-line []))

(defn event->hash [e]
  (hash-map
   :timestamp (:timestamp e)
   :x-pos (.-offsetX (:event e))
   :y-pos (.-offsetY (:event e)))) 

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
                             (fn [e] (event->hash e))
                             [(utils/listen (om/get-node owner "draw-loop-ref") "mousemove")])
            mouse-up-chan (async/map
                           (fn [e] (event->hash e))
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
      (let [current-millisecond (utils/time->delta data (:current-millisecond data))
            completed-lines (filter :selected (:complete-lines data))
            total-milliseconds (* (get-in data [:time-loop :seconds]) 1000)
            currently-drawing-pos (filter #(drawing-time-bound % current-millisecond total-milliseconds (get-in data [:time-loop :tail-in-milliseconds])) (:in-progress-line data))
            canvas (om/get-node owner "draw-loop-ref")]
        
        (utils/clear-canvas! canvas 400 400)
        
        (if (not (empty? completed-lines))
          (doseq [completed-line completed-lines]
            (let [positions (:mouse-positions completed-line)
                  draw-positions (filter #(drawing-time-bound % current-millisecond total-milliseconds(get-in data [:time-loop :tail-in-milliseconds])) positions)
                  to-draw (concat draw-positions currently-drawing-pos)]

              (doseq [point to-draw]
                (utils/canvas-draw! (:color point) (om/get-node owner "draw-loop-ref") (:x-pos point) (:y-pos point) 5 5))))
          (doseq [point currently-drawing-pos]
            (utils/canvas-draw! (:color point) (om/get-node owner "draw-loop-ref") (:x-pos point) (:y-pos point) 5 5)))))
    
    om/IRender
    (render [_]
      (if (= 3 (count (:complete-lines data)))
                     (println data))
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
    
    om/IDidMount
    (did-mount [_]
      (let [tick (fn tick []
                   (om/transact! data :current-millisecond utils/timestamp)
                   (js/requestAnimationFrame tick))]        
        (om/transact! data :initial-time utils/timestamp)        
        (tick)))
    
    om/IRender
    (render [_]
      (dom/div nil
               (om/build draw-canvas data)
               (om/build history/history-viewer data)
               (om/build playhead/time-loop data)
               (om/build mouse/mouse-position (:mouse-position data))
               #_(om/build draggable/draggable-window {:data data
                                                     :render-via ankha/inspector})))))




(defn init [state]
  (om/root 
    app
    state
    {:target (. js/document (getElementById "app"))})
  )

(init app-state/app-state)

;; (add-watch app-state/app-state :foo (fn [_ _ _ new-val] (println new-val)))



;; ! for side-effecting functions




