(ns draw-me.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.components.history :as history]
            [draw-me.components.playhead :as playhead]
            [draw-me.utils :as utils]
            [draw-me.app-state :as app-state]))

(enable-console-print!)

(defn record-mouse [data owner event]
  (let [event (assoc event :timestamp (- (:timestamp event)
                                         (:initial-time @data)))]
      (when (om/get-state owner :record-mouse)
        (om/transact! data :in-progress-line #(conj % event)))))

(defn reset-mouse-positions [data]
  (om/transact! data :complete-lines #(conj % {:mouse-positions (:in-progress-line @data)
                                               :index (inc (count %))}))
  (om/update! data :in-progress-line []))

(defn event->hash [e]
  (hash-map
   :timestamp (:timestamp e)
   :x-pos (.-offsetX (:event e))
   :y-pos (.-offsetY (:event e)))) 

(defn drawing-time-bound [position current-millisecond total-milliseconds]
  (let [tail-lifetime 500]
    (> current-millisecond (mod (:timestamp position) total-milliseconds)  (- current-millisecond tail-lifetime))))

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
            completed-lines (:complete-lines data)
            total-milliseconds (* (get-in data [:time-loop :seconds]) 1000)
            currently-drawing-pos (filter #(drawing-time-bound % current-millisecond total-milliseconds) (:in-progress-line data))]
        
        (utils/clear-canvas! (om/get-node owner "draw-loop-ref") 400 400)
        
        (doseq [completed-line completed-lines]
          (let [positions (:mouse-positions completed-line)
                draw-positions (filter #(drawing-time-bound % current-millisecond total-milliseconds) positions)
                to-draw (concat draw-positions currently-drawing-pos)]

            (doseq [point to-draw]
              (utils/canvas-draw! (om/get-node owner "draw-loop-ref") (:x-pos point) (:y-pos point) 2 2))))))
    
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
                   (om/transact! data :current-millisecond utils/timestamp)
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
  app-state/app-state
  {:target (. js/document (getElementById "app"))})

;;
;;#_(add-watch app-state/app-state :foo (fn [_ _ _ new-val] (println new-val)))
;; ! for side-effecting functions




