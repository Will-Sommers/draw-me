(ns draw-me.mouse
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))


(defn mouse-position [data owner]
  (reify

    om/IRender
    (render [_]
      (dom/div nil 
               (dom/div nil (str (:x-pos data) ", " (:y-pos data)))
               (dom/div nil "Test")))))
