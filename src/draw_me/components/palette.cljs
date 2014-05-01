(ns draw-me.components.palette
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.utils :as utils]))


(defn palette [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (dom/input #js {:id "color-input"
                               :type "color"
                               :onKeyDown #(println (.-value (. js/document (getElementById "color-input"))))})
               (dom/div #js {:className "palette"
                             :style #js {:backgroundColor (:color data)}}
                        "Test")))))
