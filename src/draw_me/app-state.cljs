(ns draw-me.app-state)

(def app-state (atom {:complete-lines {}
                      :in-progress-line []
                      :edit-line []
                      :line-count 0
                      :canvas {:width 399
                               :height 399
                               :name "draw-loop"}
                      :time-loop {:width 600
                                  :height 200
                                  :total-milliseconds 1000
                                  :tail-in-milliseconds 500}
                      :initial-time 0
                      :current-millisecond 0
                      :palette {:color "#000000"}}))

