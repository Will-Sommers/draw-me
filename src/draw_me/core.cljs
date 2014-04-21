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
                      :frames 15
                      :time-loop {:width 600
                                  :height 200}
                      :initial-time nil}))

(def fps 40)

(def render-every-n
  (/ 1000 fps))

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type (fn [event]
                             (.preventDefault event)
                             (put! out (hash-map :event event :timestamp (.getTime (new js/Date))))))
    out))

(defn canvas-draw [canvas x y x-length y-length]
  (let [context (. canvas (getContext "2d"))]
    (set! (.-fillStyle context) "#000000")
    ;; TODO figure out the exact offset 
    (.fillRect context x y x-length y-length)))

(defn draw-lines [data owner sym-name dom-node-ref]
  (let [canvas (om/get-node owner dom-node-ref)]
    (doseq [x (->> (sym-name data)
                   last
                   (:mouse-positions))]
      (canvas-draw canvas (:x-pos x) (:y-pos x) 2 2))))

(defn record-mouse [data owner pos]
  (when (om/get-state owner :record-mouse)
    (canvas-draw (om/get-node owner "draw-loop-ref") (:x-pos pos) (:y-pos pos) 2 2)
    (.log js/console (:timestamp pos))
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
                             [(listen (. js/document (getElementById "draw-loop")) "mousemove")])
            mouse-up-chan (async/map
                           (fn [e-data] (hash-map :timestamp (:timestamp e-data)  :x-pos (.-clientX (:event e-data)) :y-pos (.-clientY (:event e-data))))
                           [(listen (. js/document (getElementById "draw-loop")) "mouseup")])
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
        (draw-lines data owner :in-progress-line "draw-loop-ref"))
      #_(when (last (:complete-lines data))
        (draw-lines data owner :complete-lines "draw-loop-ref")))
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


(defn line-history [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil (dom/div nil (:index data))
               #_(apply dom/div nil (map #(dom/div nil (:pos %)) data))))))

(defn history-viewer [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:className "history"}
               (dom/div nil "History")
               (apply dom/div nil (om/build-all line-history data))))))

(defn clear-canvas [canvas w h]
  (let [context (. canvas (getContext "2d"))]
    (.clearRect context 0 0 w h)))

(defn draw-nav-time [data owner ref head-pos]
  (let [canvas (om/get-node owner ref)
        width (get-in data [:time-loop :width])
        height (get-in data [:time-loop :height])]
    
    (clear-canvas canvas width height)
    (canvas-draw canvas head-pos 0 2 height)))

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
                                                      (* (:frames data) fps)) (om/get-state owner :head))))
    om/IRender
    (render [_]
      (dom/div nil
               (dom/canvas #js {:id "time-loop"
                                :height (get-in data [:time-loop :height])
                                :width (get-in data [:time-loop :width])
                                :style #js {:border "1px solid black"}
                                :ref "time-loop-ref"})))))

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
            total-frames (* fps (:frames data))]
        (do
          (om/transact! data :initial-time #(.getTime (new js/Date)))
          (. js/window (setInterval (fn [e]
                                      (let [time-pos (om/get-state owner :time-pos)]
                                        (if (= time-pos
                                               total-frames)
                                          ()
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
                 (om/build history-viewer (:complete-lines data))
                 (om/build time-loop data {:init-state {:c-time c-time}}))))))

(om/root
  app
  app-state
  {:target (. js/document (getElementById "app"))})


#_(defn testRAF []
  (.log js/console (.getTime (new js/Date)))
  (js/requestAnimationFrame testRAF))

#_(testRAF)
