(ns draw-me.components.draggable
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [ankha.core :as ankha]
            [goog.events :as events])
  (:import [goog.events EventType]))

(enable-console-print!)

(def local-dragging? (atom false))

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type (fn [event]
                             (when @local-dragging?
                               (.preventDefault event)
                               (put! out event))))
    out))

(defn do-drag [data]
  (when-let [pos (get-in data [:data :dnd-window])]
    #js {:position "fixed"
         :left (:left pos)
         :top (:top pos)
         :display "none"}))

(defcomponent draggable-window [data owner]

  (init-state [_]
    {:c-mouse (chan)})

  (did-mount [_]
    (let [c-mouse (om/get-state owner :c-mouse)
          mouse-move-chan (async/map
                            (fn [e] [(.-clientX e) (.-clientY e)])
                            [(listen js/window "mousemove")])
          mouse-up-chan (async/map
                          (fn [e] [(.-clientX e) (.-clientY e)])
                          [(listen js/window "mouseup")])]
      (go (while true
            (alt!
              mouse-move-chan ([pos] (let [new-pos {:left (first pos) :top (last pos)}]
                                       (om/update! (:data data) :dnd-window new-pos)))
              mouse-up-chan ([pos] (reset! local-dragging? false)))))))

  (render-state [_ {:keys [c-mouse]}]
    (dom/div
      (dom/div
        {:style (do-drag data)
         :class "draggable-window"}
        (dom/div {:on-mouse-down #(reset! local-dragging? true)})
        (om/build (:render-via data) (:data data))))))
