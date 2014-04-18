(ns draw-me.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [goog.events :as events])
  (:import [goog.events EventType]))

(enable-console-print!)

(def app-state (atom {:text "Hello worlds!"
                      :complete-lines []
                      :in-progress-line []
                      :frames 15}))

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type (fn [event]
                             (.preventDefault event)
                             (put! out event)))
    out))

(defn draw-chrono-pixels [canvas x y]
  (let [context (. canvas (getContext "2d"))]
    (set! (.-fillStyle context) "#000000")
    (.fillRect context x y 2 2)))

(defn draw-lines [data owner dom-node-ref]
  (let [canvas (om/get-node owner dom-node-ref)]
    (doseq [x (last (:complete-lines data))]
      (draw-chrono-pixels canvas (first x) (second x)))))

(defn record-mouse [data owner pos]
  (when (om/get-state owner :record-mouse)
    #_(println (:in-progress-line @data))
    (om/transact! data :in-progress-line #(conj % pos))))

(defn reset-mouse-positions [data]
  (om/transact! data :complete-lines #(conj % (:in-progress-line @data)))
  (om/update! data :in-progress-line []))

(defn draw-canvas [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:c-mouse (chan)
       :mouse-positions []})
    om/IDidMount
    (did-mount [_]
      #_(draw-lines data owner "draw-loop-ref")
      (let [c-mouse (om/get-state owner :c-mouse)
            mouse-move-chan (async/map
                             (fn [e] [(.-clientX e) (.-clientY e)])
                             [(listen (. js/document (getElementById "draw-loop")) "mousemove")])
            mouse-up-chan (async/map
                           (fn [e] [(.-clientX e) (.-clientY e)])
                           [(listen (. js/document (getElementById "draw-loop")) "mouseup")])
            ]
        (go (while true
                (alt!
                 mouse-move-chan ([pos] (record-mouse data owner pos))
                 mouse-up-chan ([pos] (do
                                        (om/set-state! owner :record-mouse false)
                                        (reset-mouse-positions data)
                                        #_(om/refresh! owner)
                                        )))))))
    om/IDidUpdate
    (did-update [_ _ _]
      (when (last (:complete-lines data))
        (draw-lines data owner "draw-loop-ref")))
    om/IRender
    (render [_]
      (dom/div nil
               (dom/div nil (str (:frames data) " " "Frames"))
               (dom/canvas #js {:id "draw-loop"
                                :height 400
                                :width 400
                                :style #js {:border  "1px solid black"}
                                :ref "draw-loop-ref"
                                :onMouseDown #(do
                                                (om/set-state! owner :record-mouse true)
                                                )})))))


(defn line-history [data owner]
  (reify
    om/IRender
    (render [_]
      (.log js/console data)
      (dom/div nil (dom/div nil data)
               #_(apply dom/div nil (map #(dom/div nil (:pos %)) data))))))

(defn history-viewer [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:className "history"}
               (dom/div nil "History")
               (apply dom/div nil (om/build-all line-history data))))))

(defn app [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build draw-canvas data)
               (om/build history-viewer (:complete-lines data))))))

(om/root
  app
  app-state
  {:target (. js/document (getElementById "app"))})



