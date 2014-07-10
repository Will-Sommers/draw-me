(ns draw-me.components.mouse
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]))


(defcomponent mouse-position [data owner]

  (render [_]
    (let [mouse-pos (:mouse-position data)]
      (dom/div
        (dom/div (str (:x-pos mouse-pos) ", " (:y-pos mouse-pos)))))))
