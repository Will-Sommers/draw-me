(ns draw-me.components.mouse
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))


(defn mouse-position [data owner]
  (reify

    om/IRender
    (render [_]
      (let [mouse-pos (:mouse-position data)]
        (dom/div nil
                 (dom/div nil (str (:x-pos mouse-pos) ", " (:y-pos mouse-pos))))))))
