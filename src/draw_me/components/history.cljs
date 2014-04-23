(ns draw-me.components.history
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.app-state :as app-state]))

(defn line-history [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil (dom/div nil (str (:index data) " " "Line"))))))

(defn history-viewer [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:className "history"}
               (dom/div nil "History")
               (apply dom/div #js {:onClick (fn [e] (do
                                                   (println (:selected-lines @app-state/app-state))
                                                   (om/update! data :selected-lines [])))}
                      (om/build-all line-history (:complete-lines data)))))))
