(ns draw-me.components.playhead
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.utils :as utils]))

(defn draw-nav-time [data owner ref head-pos]
  (let [canvas (om/get-node owner ref)
        width (get-in data [:time-loop :width])
        height (get-in data [:time-loop :height])
        color "#000000"]    
    (utils/clear-canvas! canvas width height)
    (utils/canvas-draw! color canvas head-pos 0 2 height)))

(defn time-loop [data owner]
  (reify
    om/IDidUpdate
    (did-update [_ _ _]
      (let [width (get-in data [:time-loop :width])
            milliseconds (* (get-in data [:time-loop :seconds])
                            1000)
            frame-width (/ width milliseconds)
            current-time (mod (- (utils/timestamp) (:initial-time data))
                      milliseconds)
            head-position (* frame-width current-time)
            ]
        
        (draw-nav-time data owner "time-loop-ref" head-position)))

    om/IRender
    (render [_]
      (dom/div nil
               (dom/canvas #js {:id "time-loop"
                                :height (get-in data [:time-loop :height])
                                :width (get-in data [:time-loop :width])
                                :style #js {:border "1px solid black"}
                                :ref "time-loop-ref"})
               ))))
