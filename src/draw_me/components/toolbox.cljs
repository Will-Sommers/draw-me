(ns draw-me.components.toolbox
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.utils :as utils]))


(defn toolbox [data owner]
  (reify

    om/IInitState
    (init-state [_]
      {:c-drag-drop (chan)})

    om/IWillMount
    (will-mount [_]
      #_(let [c-drag-drop (om/get-state owner :c-drag-drop)]
        (while true
          (let [message (<! c-drag-drop)]
            (println "hi")))))

    om/IRenderState
    (render-state [_ state]
      (dom/div #js {:className "toolbox-wrapper"}
               (dom/div #js {:className "toolbox-header"} "Tools")))))
