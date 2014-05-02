(ns draw-me.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.components.history :as history]
            [draw-me.components.playhead :as playhead]
            [draw-me.components.draggable :as draggable]
            [draw-me.components.palette :as palette]
            [draw-me.utils :as utils]
            [draw-me.app-state :as app-state]
            [draw-me.mouse-state :as mouse-state]
            [draw-me.mouse :as mouse]
            [draw-me.edit :as edit]
            [ankha.core :as ankha]))

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

      (let [current-millisecond (utils/time->delta data)
            total-milliseconds (get-in data [:time-loop :total-milliseconds])
            canvas (om/get-node owner (str (get-in data [:canvas :name]) "-ref"))
            selected-lines (filter :selected (:complete-lines data))
            currently-drawing-pos (filter #(drawing-time-bound % current-millisecond total-milliseconds (get-in data [:time-loop :tail-in-milliseconds])) (:in-progress-line data))]
        
        (utils/clear-canvas! canvas (get-in data [:canvas :width]) (get-in data [:canvas :height]) 400)
        
        (if (not (empty? selected-lines))
          (doseq [completed-line selected-lines]
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
               (dom/div #js {:onClick #(println app-state/app-state)} "Print")
               (om/build draw-canvas data)
               (om/build history/history-viewer data)
               (om/build playhead/time-loop data)
               (om/build edit/edit-line (:edit-line data))
               (om/build palette/palette (:palette data))
               #_(om/build draggable/draggable-window {:data data
                                                     :render-via ankha/inspector})))))




(defn init [state]
  (om/root 
    app
    state
    {:target (. js/document (getElementById "app"))})

  (om/root
   mouse/mouse-position
   mouse-state/mouse-state
   {:target (. js/document (getElementById "mouse-pos"))})
  )

(init app-state/app-state)

;; (add-watch app-state/app-state :foo (fn [_ _ _ new-val] (println new-val)))
