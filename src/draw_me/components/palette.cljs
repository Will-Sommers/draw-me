(ns draw-me.components.palette
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [draw-me.utils :as utils]))


(defcomponent palette [data owner]

  (render [_]
    (dom/div
      dom/input {:id "color-input"
                     :type "color"
                     :on-key-down #(println (.-value (. js/document (getElementById "color-input"))))}
      (dom/div {:class "palette"
                    :style {:background-color (:color data)}}
        "Test"))))
