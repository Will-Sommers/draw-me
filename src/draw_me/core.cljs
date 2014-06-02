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

(defn pause [data]
  (om/transact! data [:time-loop :pause-start] utils/timestamp))

(defn play [data]
  (do
    (let [total-paused-time (- (utils/timestamp) (get-in @data [:time-loop :pause-start]))]
      (om/transact! data [:time-loop :cummulative-pause-time] (fn [] (+ (get-in @data [:time-loop :cummulative-pause-time]) total-paused-time)))
      (om/transact! data [:time-loop :pause-start] (fn [] nil)))))

(defn app [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:pause-channel (chan)})

    om/IDidMount
    (did-mount [_]
      (let [tick (fn tick []
                   (om/set-state! owner :current-millisecond (utils/timestamp))
                   (js/requestAnimationFrame tick))
            pause-channel (om/get-state owner :pause-channel)]
        (go (while true
              (let [pause-comment (<! pause-channel)]))
            (om/set-state! owner :paused? pause-comment))
        (om/transact! data :initial-time utils/timestamp)
        (tick)))

    om/IRender
    (render [_]
      (dom/div #js {:className "wrapper"}
               (dom/header nil
                           (dom/div #js {:className "name"} "A Witty Name Here"))
               (dom/div #js {:className "main"}
                        (om/build canvas/draw-canvas data {:state {:current-millisecond (om/get-state owner :current-millisecond)}})

                        (dom/div #js {:className "controls"}
                                 (dom/div #js {:onClick #(pause data)} "Pause")
                                 (dom/div #js {:onClick #(play data)} "Play")
                                 (dom/div #js {:onClick #(println app-state/app-state)} "Print"))
                        (om/build playhead/time-loop data {:state {:current-millisecond (om/get-state owner :current-millisecond) :paused? (om/get-state owner :paused?)}}))

               (dom/div #js {:className "history"}
                        (om/build history/history-viewer data))
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

(init app-state/mock)

;; (add-watch app-state/app-state :foo (fn [_ _ _ new-val] (println new-val)))
