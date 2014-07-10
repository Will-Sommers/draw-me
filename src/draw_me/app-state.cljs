(ns draw-me.app-state)

(def app-state {:complete-lines {}
                :in-progress-line []
                :edit-line []
                :line-count 0
                :canvas {:width 399
                         :height 399
                         :name "draw-loop"}
                :time-loop {:width 599
                            :height 99
                            :loop-in-milliseconds 2000
                            :tail-in-milliseconds 500
                            :pause-start nil
                            :cummulative-pause-time 10}
                :initial-time 0
                :palette {:draw-color "#000000"
                          :hightlight-color "#067300"}})
