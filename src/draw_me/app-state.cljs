(ns draw-me.app-state)

(def app-state (atom {:text "Hell worlds!"
                      :complete-lines []
                      :in-progress-line []
                      :selected-line []
                      :time-loop {:width 600
                                  :height 200
                                  :total-milliseconds 2000
                                  :tail-in-milliseconds 500}
                      :initial-time 0
                      :current-millisecond 0
                      :mouse-position {:x-pos 0 :y-pos 0}
                      :palette {:color "#FF0000"}}))

(def mock-state
  (atom
   {
    :mouse-position {:x-pos 224, :y-pos 110}, :initial-time 1398367761958, :palette {:color "#000000"}, :time-loop {:width 600, :height 200, :seconds 1, :tail-in-milliseconds 500}, :selected-line [], :complete-lines [{:mouse-positions [{:timestamp 1476, :x-pos 163, :y-pos 116, :color "#FF0000"} {:timestamp 1496, :x-pos 161, :y-pos 116, :color "#FF0000"} {:timestamp 1511, :x-pos 159, :y-pos 117, :color "#FF0000"} {:timestamp 1526, :x-pos 155, :y-pos 121, :color "#FF0000"} {:timestamp 1541, :x-pos 152, :y-pos 127, :color "#FF0000"} {:timestamp 1559, :x-pos 149, :y-pos 132, :color "#FF0000"} {:timestamp 1576, :x-pos 145, :y-pos 138, :color "#FF0000"} {:timestamp 1592, :x-pos 139, :y-pos 145, :color "#FF0000"} {:timestamp 1612, :x-pos 135, :y-pos 156, :color "#FF0000"} {:timestamp 1625, :x-pos 133, :y-pos 161, :color "#FF0000"} {:timestamp 1643, :x-pos 130, :y-pos 168, :color "#FF0000"} {:timestamp 1660, :x-pos 128, :y-pos 175, :color "#FF0000"} {:timestamp 1680, :x-pos 127, :y-pos 181, :color "#FF0000"} {:timestamp 1698, :x-pos 126, :y-pos 187, :color "#FF0000"} {:timestamp 1714, :x-pos 126, :y-pos 194, :color "#FF0000"} {:timestamp 1734, :x-pos 126, :y-pos 202, :color "#FF0000"} {:timestamp 1748, :x-pos 126, :y-pos 211, :color "#FF0000"} {:timestamp 1764, :x-pos 129, :y-pos 216, :color "#FF0000"} {:timestamp 1781, :x-pos 133, :y-pos 223, :color "#FF0000"} {:timestamp 1798, :x-pos 139, :y-pos 229, :color "#FF0000"} {:timestamp 1815, :x-pos 144, :y-pos 235, :color "#FF0000"} {:timestamp 1833, :x-pos 148, :y-pos 239, :color "#FF0000"} {:timestamp 1858, :x-pos 157, :y-pos 244, :color "#FF0000"} {:timestamp 1874, :x-pos 167, :y-pos 250, :color "#FF0000"} {:timestamp 1892, :x-pos 178, :y-pos 259, :color "#FF0000"} {:timestamp 1908, :x-pos 187, :y-pos 264, :color "#FF0000"} {:timestamp 1924, :x-pos 195, :y-pos 268, :color "#FF0000"} {:timestamp 1941, :x-pos 200, :y-pos 269, :color "#FF0000"} {:timestamp 1960, :x-pos 205, :y-pos 270, :color "#FF0000"} {:timestamp 1975, :x-pos 212, :y-pos 270, :color "#FF0000"} {:timestamp 1992, :x-pos 222, :y-pos 263, :color "#FF0000"} {:timestamp 2009, :x-pos 230, :y-pos 256, :color "#FF0000"} {:timestamp 2026, :x-pos 239, :y-pos 243, :color "#FF0000"} {:timestamp 2042, :x-pos 245, :y-pos 230, :color "#FF0000"} {:timestamp 2058, :x-pos 250, :y-pos 214, :color "#FF0000"} {:timestamp 2075, :x-pos 254, :y-pos 197, :color "#FF0000"} {:timestamp 2092, :x-pos 256, :y-pos 177, :color "#FF0000"} {:timestamp 2109, :x-pos 256, :y-pos 164, :color "#FF0000"} {:timestamp 2126, :x-pos 256, :y-pos 147, :color "#FF0000"} {:timestamp 2143, :x-pos 256, :y-pos 132, :color "#FF0000"} {:timestamp 2160, :x-pos 254, :y-pos 127, :color "#FF0000"} {:timestamp 2177, :x-pos 249, :y-pos 120, :color "#FF0000"} {:timestamp 2193, :x-pos 242, :y-pos 116, :color "#FF0000"} {:timestamp 2210, :x-pos 234, :y-pos 113, :color "#FF0000"} {:timestamp 2226, :x-pos 224, :y-pos 110, :color "#FF0000"}], :index 1, :selected true}], :in-progress-line [], :text nil, :current-millisecond 1398367764205} 
))
