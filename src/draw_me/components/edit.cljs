(ns draw-me.components.edit
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))




(defcomponent edit-line [data owner]

  (render [_]
    (dom/div {:class "edit-line-group.sidebar-group"}
      (dom/div {:class "sidebar-header-group"}
        (dom/div {:class "sidebar-header"} "Edit Line")))))
