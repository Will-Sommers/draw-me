(ns draw-me.components.playhead
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.utils :as utils]))

(defn draw-nav-time [data owner ref head-pos]
  (let [canvas (om/get-node owner ref)
        width (get-in data [:time-loop :width])
        height (get-in data [:time-loop :height])]
    
    (utils/clear-canvas canvas width height)
    (utils/canvas-draw canvas head-pos 0 2 height)))

(defn time-loop [data owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (let [c-time (om/get-state owner :c-time)]
        (go (while true (let [val (<! c-time)]
                          (om/set-state! owner :head val))))))
    om/IDidUpdate
    (did-update [_ _ _]
      (draw-nav-time data owner "time-loop-ref" (* (/ (get-in data [:time-loop :width])
                                                      (* (:frames data) 40)) (om/get-state owner :head))))
    om/IRender
    (render [_]
      (dom/div nil
               (dom/canvas #js {:id "time-loop"
                                :height (get-in data [:time-loop :height])
                                :width (get-in data [:time-loop :width])
                                :style #js {:border "1px solid black"}
                                :ref "time-loop-ref"})))))
