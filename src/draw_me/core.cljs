(ns draw-me.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.components.history :as history]
            [draw-me.components.playhead :as playhead]
            [draw-me.components.draggable :as draggable]
            [draw-me.components.palette :as palette]
            [draw-me.components.canvas :as canvas]
            [draw-me.utils :as utils]
            [draw-me.app-state :as app-state]
            [draw-me.mouse-state :as mouse-state]
            [draw-me.mouse :as mouse]
            [draw-me.edit :as edit]
            [ankha.core :as ankha]))

(enable-console-print!)

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
               (om/build canvas/draw-canvas data)
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
