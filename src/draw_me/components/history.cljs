(ns draw-me.components.history
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn line-history [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil (dom/div nil (:index data))))))

(defn history-viewer [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:className "history"}
               (dom/div nil "History")
               (apply dom/div nil (om/build-all line-history data))))))
