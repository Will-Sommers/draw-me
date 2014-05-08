(ns draw-me.components.history
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.app-state :as app-state]))

(defn history-item-style [selected?]
  (if selected?
    #js {:backgroundColor "#ccc"}
    #js {:backgroundColor "#fff"}))

(defn global-toggle-select [data bool]
  (om/transact! data :complete-lines (fn [data]
                                       (into [] (map #(assoc % :selected bool) data)))))

(defn select [data e]
  (om/transact! data :selected (fn [selected] (not selected))))

(defn begin-edit [data]
  (om/update! data :edit-line @data))

(defn delete-line [data line]
  (om/transact! data :complete-lines #(dissoc (:complete-lines @data) (key line))))

(defn line-history [data owner]
  (reify

    om/IRenderState
    (render-state [_ _]
      (let [c-delete-line (om/get-state owner :c-delete-line)]
        (dom/div nil
                 (dom/span #js {:onClick #(select data %)
                                :onMouseEnter #(om/transact! data :hover (fn [hover] true))
                                :onMouseLeave #(om/transact! data :hover (fn [hover] false))
                               :style (history-item-style (:selected data))}
                          (str "Line " (key data)))
                 (dom/span #js {:onClick #(begin-edit data)} "Edit")
                 (dom/span #js {:onClick #(put! c-delete-line data)} "Delete"))))))

(defn history-viewer [data owner]
  (reify

    om/IInitState
    (init-state [_]
      {:c-delete-line (chan)})

    om/IDidMount
    (did-mount [_]
      (let [c-delete-line (om/get-state owner :c-delete-line)]
        (go (while true
              (let [line (<! c-delete-line)]
                (delete-line data line))))))
    
    om/IRender
    (render [_]
      (let [c-delete-line (om/get-state owner :c-delete-line)]
        (dom/div #js {:className "history"}
                 (dom/div nil
                          (dom/span nil "History")
                          (dom/span #js {:onClick #(global-toggle-select data true)}  "-Select All-")
                          (dom/span #js {:onClick #(global-toggle-select data false)} "Select None"))
                 (apply dom/div nil 
                        (om/build-all line-history (:complete-lines data)
                                      {:init-state {:c-delete-line c-delete-line}})))))))
