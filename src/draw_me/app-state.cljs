(ns draw-me.app-state)

(def app-state (atom {:text "Hell worlds!"
                      :complete-lines []
                      :in-progress-line []
                      :selected-lines []
                      :time-loop {:width 600
                                  :height 200
                                  :seconds 1}
                      :frame 0
                      :initial-time 10
                      :previous-millisecond 0
                      :current-millisecond 0
                      :mouse-position {:x-pos nil :y-pos nil}}))
