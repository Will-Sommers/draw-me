(ns draw-me.components.history
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [draw-me.app-state :as app-state]))

(defn history-item-style [selected?]
  (if selected?
    #js {:color "#BFBFBF"}
    #js {:color "#292929"}))

(defn global-toggle-select [data bool]
  (om/transact! data :complete-lines (fn [data]
                                       (into {} (map #(hash-map (key %) (assoc (val %) :selected bool)) data)))))

(defn select [data]
  (om/transact! data :selected (fn [selected] (not selected))))

(defn begin-edit [data]
  (om/update! data :edit-line @data))

(defn delete-line [data line]
  (om/transact! data :complete-lines #(dissoc (:complete-lines @data) (symbol (:hash @line)))))

(defcomponent line-history [data owner]

  (render-state [_ {:keys [c-delete-line]}]

    (dom/div {:class "line"
              :style (history-item-style (:selected data))}
      (dom/span {:on-click #(select data)
                 :on-mouse-enter #(om/transact! data :hover (fn [hover] true))
                 :on-mouse-leave #(om/transact! data :hover (fn [hover] false))
                 }
        (str "Line " (:hash data)))
      (dom/span {:on-click #(begin-edit data)} "Edit")
      (dom/span {:on-click #(put! c-delete-line data)} "Delete"))))

(defcomponent history-viewer [data owner]

  (init-state [_]
    {:c-delete-line (chan)})

  (did-mount [_]
    (let [c-delete-line (om/get-state owner :c-delete-line)]
      (go (while true
            (let [line (<! c-delete-line)]
              (delete-line data line))))))

  (render-state [_ {:keys [c-delete-line]}]

    (dom/div {:class  "history-group"}
      (dom/div {:class "sidebar-header-group"}
        (dom/div {:class "sidebar-header"} "History")
        (dom/div {:class "history-options"}
          (dom/span {:on-click #(global-toggle-select data true)}  "All")
          (dom/span nil "/")
          (dom/span {:on-click #(global-toggle-select data false)} "None")))
      (dom/div
        (om/build-all line-history (vals (:complete-lines data))
          {:init-state {:c-delete-line c-delete-line}})))))
