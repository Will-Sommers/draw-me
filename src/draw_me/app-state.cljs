(ns draw-me.app-state)

(def app-state (atom {:text "Hell worlds!"
                      :complete-lines []
                      :in-progress-line []
                      :time-loop {:width 600
                                  :height 200
                                  :seconds 5}
                      :frame 0
                      :initial-time nil}))
