(ns draw-me.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [draw-me.components.history :as history]
            [draw-me.components.playhead :as playhead]
            [draw-me.components.draggable :as draggable]
            [draw-me.components.palette :as palette]
            [draw-me.components.canvas :as canvas]
            [draw-me.components.toolbox :as toolbox]
            [draw-me.utils :as utils]
            [draw-me.app-state :as app-state]
            [draw-me.mouse-state :as mouse-state]
            [draw-me.components.mouse :as mouse]
            [draw-me.components.edit :as edit]
            [ankha.core :as ankha]))

(enable-console-print!)

(defn pause [data]
  (om/transact! data [:time-loop :pause-start] utils/timestamp))

(defn play [data]
  (let [total-paused-time (- (utils/timestamp) (get-in @data [:time-loop :pause-start]))]
    (om/transact! data [:time-loop :cummulative-pause-time] (fn [] (+ (get-in @data [:time-loop :cummulative-pause-time]) total-paused-time)))
    (om/transact! data [:time-loop :pause-start] (fn [] nil))))

(defcomponent app [data owner]

  (init-state [_]
    {:pause-channel (chan)})

  (render [_]

    (om/set-state! owner :current-millisecond (utils/timestamp))

    (dom/div {:class "wrapper"}
      (dom/header
        (dom/div {:class "name"} "A Witty Name Here"))
      (dom/div {:class "main"}
        (om/build canvas/draw-canvas data {:state {:current-millisecond (om/get-state owner :current-millisecond)}})

        (dom/div {:class "controls"}
          (if (nil? (get-in data [:time-loop :pause-start]))
            (dom/div {:on-click #(pause data)} "Pause")
            (dom/div {:on-click #(play data)} "Play"))
          (dom/div {:on-click #(println app-state/app-state)} "Print"))
        (om/build playhead/time-loop data {:state {:current-millisecond (om/get-state owner :current-millisecond) :paused? (om/get-state owner :paused?)}}))

      (dom/div  {:class "right-sidebar"}
        (om/build history/history-viewer data)
        (om/build edit/edit-line data))
      (om/build toolbox/toolbox data)
      #_(om/build draggable/draggable-window {:data data
                                              :render-via ankha/inspector}))))




(defn init [state]
  (om/root
    app
    state
    {:target (. js/document (getElementById "app"))})

  (om/root
   mouse/mouse-position
   mouse-state/mouse-state
   {:target (. js/document (getElementById "mouse-pos"))}))

(init (atom app-state/app-state))
