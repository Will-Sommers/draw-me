(ns draw-me.edit
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))




(defn edit-line [data owner]
  (reify

    om/IRender
    (render [_]
      (dom/div nil (:index data)))))
