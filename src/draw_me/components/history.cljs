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

(defn line-history [data owner]
  (reify
    
    om/IRender
    (render [_]
      (let [selected-lines (om/get-state owner :selected)]
        (dom/div nil
                 (dom/span #js {:onClick #(select data %)
                               :style (history-item-style (:selected data))}
                          (str "Line " (:index data)))
                 (dom/span #js {:onClick #(begin-edit data)} "Edit"))))))

(defn history-viewer [data owner]
  (reify
    
    om/IRender
    (render [_]
      (dom/div #js {:className "history"}
               (dom/div nil
                        (dom/span nil "History")
                        (dom/span #js {:onClick #(global-toggle-select data true)}  "-Select All-")
                        (dom/span #js {:onClick #(global-toggle-select data false)} "Select None"))
               (apply dom/div nil 
                      (om/build-all line-history (:complete-lines data)))
               ))))
