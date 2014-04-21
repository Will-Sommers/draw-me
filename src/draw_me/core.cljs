(ns draw-me.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [draw-me.components.history :as history]
            [draw-me.components.playhead :as playhead]
            [draw-me.utils :as utils]))

(enable-console-print!)

(def app-state (atom {:text "Hell worlds!"
                      :complete-lines []
                      :in-progress-line []
                      :frames 15
                      :time-loop {:width 600
                                  :height 200}
                      :initial-time nil}))

(def render-every-n
  (/ 1000 40))

(defn record-mouse [data owner pos]
  (when (om/get-state owner :record-mouse)
    (utils/canvas-draw (om/get-node owner "draw-loop-ref") (:x-pos pos) (:y-pos pos) 2 2)
    (om/transact! data :in-progress-line #(conj % pos))))

(defn reset-mouse-positions [data]
  (om/transact! data :complete-lines #(conj % {:mouse-positions (:in-progress-line @data)
                                               :index (inc (count %))}))
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
                             (fn [e-data]
                               (do
                                 (hash-map :timestamp (:timestamp e-data)  :x-pos (.-clientX (:event e-data)) :y-pos (.-clientY (:event e-data)))))
                             [(utils/listen (. js/document (getElementById "draw-loop")) "mousemove")])
            mouse-up-chan (async/map
                           (fn [e-data] (hash-map :timestamp (:timestamp e-data)  :x-pos (.-clientX (:event e-data)) :y-pos (.-clientY (:event e-data))))
                           [(utils/listen (. js/document (getElementById "draw-loop")) "mouseup")])
            ]
        (go (while true
                (alt!
                 mouse-move-chan ([pos] (record-mouse data owner pos))
                 mouse-up-chan ([pos] (do
                                        (om/set-state! owner :record-mouse false)
                                        (reset-mouse-positions data)
                                        )))))))
    om/IDidUpdate
    (did-update [_ _ _]
      (when (not (empty? (:in-progress-line data)))
        (utils/draw-lines data owner :in-progress-line  (om/get-node owner "draw-loop-ref")))
      #_(when (last (:complete-lines data))
        (utils/draw-lines data owner :complete-lines  (om/get-node owner "draw-loop-ref"))))
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
                                                (om/set-state! owner :record-mouse true))})))))

(defn app [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:c-time (chan)
       :c-loop (chan)
       :time-pos 0})
    om/IWillMount
    (will-mount [_]
      (let [c-time (om/get-state owner :c-time)
            total-frames (* 40 (:frames data))]
        (do
          (om/transact! data :initial-time #(.getTime (new js/Date)))
          (. js/window (setInterval (fn [e]
                                      (let [time-pos (om/get-state owner :time-pos)]
                                        (if (= time-pos
                                               total-frames)
                                          (om/set-state! owner :time-pos 0)
                                          (om/set-state! owner :time-pos (inc time-pos))
                                          
                                          )
                                        
                                        (put! c-time time-pos))
                                      ) render-every-n)))))
    om/IRender
    (render [_]
      (let [c-time (om/get-state owner :c-time)]
        (dom/div nil
                 (om/build draw-canvas data)
                 (om/build history/history-viewer (:complete-lines data))
                 (om/build playhead/time-loop data {:init-state {:c-time c-time}}))))))

(om/root
  app
  app-state
  {:target (. js/document (getElementById "app"))})


#_(defn testRAF []
  (.log js/console (.getTime (new js/Date)))
  (js/requestAnimationFrame testRAF))

#_(testRAF)
