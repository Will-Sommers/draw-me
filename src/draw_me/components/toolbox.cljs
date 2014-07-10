(ns draw-me.components.toolbox
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [draw-me.utils :as utils :refer [log loge]]))

(def style-hash
  #js {:top 0
       :left 0
       :width "100%"
       :height "100%"
       :position "fixed"
       :z-index 5})

(defn pointer-style [dragging?]
  (if dragging?
    #js {:cursor "-webkit-grab"}
    #js {:cursor "pointer"}))

(defn update-position [event owner]
  (let [properties [[:left "clientX" (.. event -target -scrollWidth)]
                    [:top "clientY" (.. event -target -scrollHeight)]]
        center-target (fn [property dimension]
                        (- property (/ dimension 2)))]
    (doseq [[field property dimension] properties]
      (om/set-state! owner field (center-target (aget event property) dimension)))))



(defcomponent toolbox [data owner]

  (init-state [_]
    {:c-drag-drop (chan)
     :dragging? false
     :open? true
     :left "600"
     :top "150"})

  (will-mount [_]
    (let [c-drag-drop (om/get-state owner :c-drag-drop)]
      (go (while true
            (let [[command message] (<! c-drag-drop)]
              (condp = command
                :drag-start (utils/log "hil")))))))

  (render-state [_ {:keys [c-drag-drop
                           dragging?
                           left
                           top
                           open?]}]
    (dom/div
      (if dragging?
        (dom/div {:style style-hash}))
      (dom/div {:class (dom/class-set {"toolbox-wrapper" true
                                       "toolbox-min" (not open?)
                                       "toolbox-max" open?})
                :style {:left left
                        :top top}}
        (dom/div {:class "toolbox-header"}
          (dom/div {:style (pointer-style dragging?)
                    :on-mouse-down #(om/set-state! owner :dragging? true)
                    :on-mouse-move #(if dragging?
                                      (update-position % owner))
                    :on-mouse-out #(if dragging?
                                     (update-position % owner))
                    :on-mouse-up #(om/set-state! owner :dragging? false)}"Tools")
          (dom/i {:class (dom/class-set {"fa" true
                                         (if open?
                                           "fa-chevron-up"
                                           "fa-chevron-down") true
                                         "toggle-toolbox" true})
                  :on-click #(do
                               (log %)
                               (. % (stopPropagation))
                               (log "hi")
                               (om/set-state! owner :open? (not open?)))}))))))
