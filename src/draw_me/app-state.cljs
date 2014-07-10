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


(def mock (atom {:line-count 6, :initial-time 1400020736106, :edit-line [], :canvas {:width 399, :height 399, :name "draw-loop"}, :palette {:draw-color "#000000", :hightlight-color "#067300"}, :time-loop {:width 600, :height 100, :loop-in-milliseconds 4000, :tail-in-milliseconds 500, :pause-start nil, :cummulative-pause-time 10}, :complete-lines {1 {:mouse-positions [{:timestamp 1229, :x-pos 31, :y-pos 91, :color nil} {:timestamp 1246, :x-pos 32, :y-pos 91, :color nil} {:timestamp 1262, :x-pos 34, :y-pos 91, :color nil} {:timestamp 1279, :x-pos 35, :y-pos 91, :color nil} {:timestamp 1296, :x-pos 36, :y-pos 91, :color nil} {:timestamp 1313, :x-pos 38, :y-pos 91, :color nil} {:timestamp 1330, :x-pos 39, :y-pos 91, :color nil} {:timestamp 1348, :x-pos 41, :y-pos 91, :color nil} {:timestamp 1364, :x-pos 43, :y-pos 91, :color nil} {:timestamp 1381, :x-pos 45, :y-pos 91, :color nil} {:timestamp 1398, :x-pos 46, :y-pos 91, :color nil} {:timestamp 1415, :x-pos 49, :y-pos 91, :color nil} {:timestamp 1433, :x-pos 50, :y-pos 91, :color nil} {:timestamp 1455, :x-pos 51, :y-pos 91, :color nil} {:timestamp 1472, :x-pos 52, :y-pos 93, :color nil} {:timestamp 1491, :x-pos 52, :y-pos 95, :color nil} {:timestamp 1513, :x-pos 52, :y-pos 99, :color nil} {:timestamp 1530, :x-pos 52, :y-pos 105, :color nil} {:timestamp 1547, :x-pos 52, :y-pos 114, :color nil} {:timestamp 1564, :x-pos 50, :y-pos 122, :color nil} {:timestamp 1579, :x-pos 46, :y-pos 131, :color nil} {:timestamp 1597, :x-pos 42, :y-pos 141, :color nil} {:timestamp 1614, :x-pos 38, :y-pos 149, :color nil} {:timestamp 1630, :x-pos 35, :y-pos 155, :color nil} {:timestamp 1647, :x-pos 31, :y-pos 162, :color nil} {:timestamp 1663, :x-pos 29, :y-pos 168, :color nil} {:timestamp 1681, :x-pos 28, :y-pos 172, :color nil} {:timestamp 1698, :x-pos 27, :y-pos 175, :color nil} {:timestamp 1713, :x-pos 26, :y-pos 178, :color nil} {:timestamp 1731, :x-pos 25, :y-pos 180, :color nil} {:timestamp 1747, :x-pos 25, :y-pos 181, :color nil} {:timestamp 1764, :x-pos 24, :y-pos 182, :color nil} {:timestamp 1782, :x-pos 24, :y-pos 184, :color nil} {:timestamp 1797, :x-pos 23, :y-pos 185, :color nil} {:timestamp 1814, :x-pos 22, :y-pos 186, :color nil} {:timestamp 1831, :x-pos 22, :y-pos 187, :color nil}], :hash 1, :selected true}, 2 {:mouse-positions [{:timestamp 2315, :x-pos 36, :y-pos 141, :color nil} {:timestamp 2399, :x-pos 37, :y-pos 141, :color nil} {:timestamp 2433, :x-pos 38, :y-pos 141, :color nil} {:timestamp 2450, :x-pos 39, :y-pos 141, :color nil} {:timestamp 2466, :x-pos 40, :y-pos 141, :color nil} {:timestamp 2483, :x-pos 42, :y-pos 141, :color nil} {:timestamp 2500, :x-pos 43, :y-pos 141, :color nil} {:timestamp 2516, :x-pos 45, :y-pos 141, :color nil} {:timestamp 2533, :x-pos 47, :y-pos 141, :color nil} {:timestamp 2550, :x-pos 50, :y-pos 141, :color nil} {:timestamp 2567, :x-pos 52, :y-pos 141, :color nil} {:timestamp 2584, :x-pos 55, :y-pos 141, :color nil} {:timestamp 2601, :x-pos 57, :y-pos 141, :color nil} {:timestamp 2617, :x-pos 59, :y-pos 141, :color nil} {:timestamp 2634, :x-pos 62, :y-pos 140, :color nil} {:timestamp 2651, :x-pos 65, :y-pos 140, :color nil} {:timestamp 2669, :x-pos 69, :y-pos 139, :color nil} {:timestamp 2686, :x-pos 73, :y-pos 139, :color nil} {:timestamp 2716, :x-pos 77, :y-pos 139, :color nil} {:timestamp 2720, :x-pos 80, :y-pos 139, :color nil} {:timestamp 2744, :x-pos 83, :y-pos 138, :color nil} {:timestamp 2760, :x-pos 86, :y-pos 137, :color nil} {:timestamp 2779, :x-pos 89, :y-pos 136, :color nil} {:timestamp 2801, :x-pos 92, :y-pos 136, :color nil} {:timestamp 2818, :x-pos 93, :y-pos 134, :color nil} {:timestamp 2833, :x-pos 95, :y-pos 134, :color nil} {:timestamp 2851, :x-pos 95, :y-pos 133, :color nil} {:timestamp 2867, :x-pos 96, :y-pos 132, :color nil} {:timestamp 2884, :x-pos 97, :y-pos 131, :color nil} {:timestamp 2901, :x-pos 98, :y-pos 131, :color nil} {:timestamp 2917, :x-pos 99, :y-pos 130, :color nil} {:timestamp 2934, :x-pos 99, :y-pos 129, :color nil} {:timestamp 2952, :x-pos 100, :y-pos 125, :color nil} {:timestamp 2968, :x-pos 102, :y-pos 120, :color nil} {:timestamp 2984, :x-pos 102, :y-pos 114, :color nil} {:timestamp 3002, :x-pos 104, :y-pos 108, :color nil} {:timestamp 3018, :x-pos 104, :y-pos 105, :color nil} {:timestamp 3035, :x-pos 104, :y-pos 102, :color nil} {:timestamp 3052, :x-pos 105, :y-pos 100, :color nil} {:timestamp 3085, :x-pos 105, :y-pos 100, :color nil} {:timestamp 3186, :x-pos 104, :y-pos 100, :color nil} {:timestamp 3201, :x-pos 103, :y-pos 100, :color nil} {:timestamp 3218, :x-pos 102, :y-pos 102, :color nil} {:timestamp 3235, :x-pos 100, :y-pos 104, :color nil} {:timestamp 3251, :x-pos 97, :y-pos 110, :color nil} {:timestamp 3269, :x-pos 95, :y-pos 116, :color nil} {:timestamp 3286, :x-pos 92, :y-pos 129, :color nil} {:timestamp 3303, :x-pos 90, :y-pos 141, :color nil} {:timestamp 3319, :x-pos 89, :y-pos 150, :color nil} {:timestamp 3336, :x-pos 86, :y-pos 163, :color nil} {:timestamp 3353, :x-pos 84, :y-pos 169, :color nil} {:timestamp 3370, :x-pos 82, :y-pos 179, :color nil} {:timestamp 3387, :x-pos 80, :y-pos 186, :color nil} {:timestamp 3404, :x-pos 79, :y-pos 191, :color nil} {:timestamp 3421, :x-pos 77, :y-pos 194, :color nil} {:timestamp 3439, :x-pos 75, :y-pos 198, :color nil} {:timestamp 3456, :x-pos 74, :y-pos 200, :color nil} {:timestamp 3472, :x-pos 73, :y-pos 200, :color nil} {:timestamp 3496, :x-pos 72, :y-pos 201, :color nil} {:timestamp 3514, :x-pos 71, :y-pos 202, :color nil} {:timestamp 3532, :x-pos 70, :y-pos 202, :color nil} {:timestamp 3549, :x-pos 69, :y-pos 202, :color nil} {:timestamp 3568, :x-pos 68, :y-pos 202, :color nil} {:timestamp 3586, :x-pos 66, :y-pos 202, :color nil} {:timestamp 3604, :x-pos 64, :y-pos 202, :color nil} {:timestamp 3619, :x-pos 63, :y-pos 202, :color nil} {:timestamp 3636, :x-pos 62, :y-pos 201, :color nil} {:timestamp 3653, :x-pos 61, :y-pos 199, :color nil} {:timestamp 3670, :x-pos 60, :y-pos 194, :color nil} {:timestamp 3687, :x-pos 59, :y-pos 190, :color nil} {:timestamp 3702, :x-pos 59, :y-pos 187, :color nil} {:timestamp 3720, :x-pos 59, :y-pos 183, :color nil} {:timestamp 3736, :x-pos 59, :y-pos 179, :color nil} {:timestamp 3753, :x-pos 59, :y-pos 177, :color nil} {:timestamp 3770, :x-pos 59, :y-pos 175, :color nil} {:timestamp 3787, :x-pos 61, :y-pos 173, :color nil} {:timestamp 3804, :x-pos 62, :y-pos 172, :color nil} {:timestamp 3821, :x-pos 63, :y-pos 172, :color nil} {:timestamp 3836, :x-pos 64, :y-pos 172, :color nil} {:timestamp 3854, :x-pos 65, :y-pos 171, :color nil} {:timestamp 3872, :x-pos 66, :y-pos 171, :color nil} {:timestamp 3937, :x-pos 67, :y-pos 171, :color nil} {:timestamp 3954, :x-pos 68, :y-pos 171, :color nil} {:timestamp 3971, :x-pos 69, :y-pos 171, :color nil} {:timestamp 3988, :x-pos 72, :y-pos 171, :color nil} {:timestamp 4004, :x-pos 75, :y-pos 172, :color nil} {:timestamp 4020, :x-pos 78, :y-pos 175, :color nil} {:timestamp 4038, :x-pos 81, :y-pos 177, :color nil} {:timestamp 4055, :x-pos 83, :y-pos 178, :color nil} {:timestamp 4070, :x-pos 85, :y-pos 180, :color nil} {:timestamp 4087, :x-pos 86, :y-pos 180, :color nil} {:timestamp 4105, :x-pos 87, :y-pos 182, :color nil} {:timestamp 4122, :x-pos 88, :y-pos 183, :color nil} {:timestamp 4138, :x-pos 90, :y-pos 183, :color nil} {:timestamp 4155, :x-pos 90, :y-pos 184, :color nil} {:timestamp 4172, :x-pos 91, :y-pos 184, :color nil} {:timestamp 4189, :x-pos 93, :y-pos 184, :color nil} {:timestamp 4206, :x-pos 94, :y-pos 184, :color nil} {:timestamp 4224, :x-pos 97, :y-pos 184, :color nil} {:timestamp 4250, :x-pos 99, :y-pos 184, :color nil} {:timestamp 4258, :x-pos 102, :y-pos 182, :color nil} {:timestamp 4288, :x-pos 109, :y-pos 177, :color nil} {:timestamp 4300, :x-pos 113, :y-pos 173, :color nil} {:timestamp 4318, :x-pos 118, :y-pos 168, :color nil} {:timestamp 4335, :x-pos 122, :y-pos 164, :color nil} {:timestamp 4351, :x-pos 126, :y-pos 160, :color nil} {:timestamp 4372, :x-pos 130, :y-pos 155, :color nil} {:timestamp 4390, :x-pos 132, :y-pos 153, :color nil} {:timestamp 4405, :x-pos 134, :y-pos 148, :color nil} {:timestamp 4421, :x-pos 137, :y-pos 146, :color nil} {:timestamp 4439, :x-pos 139, :y-pos 145, :color nil} {:timestamp 4455, :x-pos 139, :y-pos 143, :color nil} {:timestamp 4471, :x-pos 140, :y-pos 142, :color nil} {:timestamp 4488, :x-pos 141, :y-pos 142, :color nil} {:timestamp 4505, :x-pos 142, :y-pos 141, :color nil} {:timestamp 4626, :x-pos 141, :y-pos 142, :color nil} {:timestamp 4642, :x-pos 138, :y-pos 147, :color nil} {:timestamp 4659, :x-pos 136, :y-pos 151, :color nil} {:timestamp 4685, :x-pos 132, :y-pos 159, :color nil} {:timestamp 4702, :x-pos 130, :y-pos 165, :color nil} {:timestamp 4719, :x-pos 126, :y-pos 170, :color nil} {:timestamp 4736, :x-pos 124, :y-pos 175, :color nil} {:timestamp 4754, :x-pos 123, :y-pos 178, :color nil} {:timestamp 4772, :x-pos 122, :y-pos 182, :color nil} {:timestamp 4789, :x-pos 122, :y-pos 184, :color nil} {:timestamp 4804, :x-pos 122, :y-pos 185, :color nil} {:timestamp 4822, :x-pos 122, :y-pos 186, :color nil}], :hash 2, :selected true}, 3 {:mouse-positions [{:timestamp 5694, :x-pos 151, :y-pos 122, :color nil} {:timestamp 5709, :x-pos 151, :y-pos 121, :color nil} {:timestamp 5726, :x-pos 151, :y-pos 120, :color nil} {:timestamp 5743, :x-pos 151, :y-pos 119, :color nil} {:timestamp 5760, :x-pos 151, :y-pos 118, :color nil} {:timestamp 5777, :x-pos 151, :y-pos 117, :color nil} {:timestamp 5795, :x-pos 150, :y-pos 117, :color nil} {:timestamp 5811, :x-pos 149, :y-pos 117, :color nil} {:timestamp 5828, :x-pos 147, :y-pos 117, :color nil} {:timestamp 5845, :x-pos 146, :y-pos 117, :color nil} {:timestamp 5862, :x-pos 144, :y-pos 116, :color nil} {:timestamp 5879, :x-pos 143, :y-pos 116, :color nil} {:timestamp 5908, :x-pos 142, :y-pos 116, :color nil} {:timestamp 5924, :x-pos 141, :y-pos 116, :color nil} {:timestamp 5942, :x-pos 140, :y-pos 116, :color nil} {:timestamp 5959, :x-pos 139, :y-pos 116, :color nil} {:timestamp 5993, :x-pos 138, :y-pos 117, :color nil} {:timestamp 6010, :x-pos 138, :y-pos 119, :color nil} {:timestamp 6026, :x-pos 138, :y-pos 120, :color nil} {:timestamp 6043, :x-pos 138, :y-pos 122, :color nil} {:timestamp 6061, :x-pos 138, :y-pos 123, :color nil} {:timestamp 6078, :x-pos 138, :y-pos 124, :color nil} {:timestamp 6110, :x-pos 139, :y-pos 125, :color nil} {:timestamp 6127, :x-pos 140, :y-pos 125, :color nil} {:timestamp 6144, :x-pos 141, :y-pos 125, :color nil} {:timestamp 6162, :x-pos 142, :y-pos 126, :color nil} {:timestamp 6177, :x-pos 143, :y-pos 126, :color nil} {:timestamp 6208, :x-pos 143, :y-pos 126, :color nil} {:timestamp 6214, :x-pos 145, :y-pos 126, :color nil} {:timestamp 6245, :x-pos 145, :y-pos 126, :color nil} {:timestamp 6261, :x-pos 146, :y-pos 126, :color nil} {:timestamp 6277, :x-pos 147, :y-pos 126, :color nil} {:timestamp 6295, :x-pos 148, :y-pos 126, :color nil} {:timestamp 6315, :x-pos 149, :y-pos 125, :color nil} {:timestamp 6332, :x-pos 150, :y-pos 124, :color nil} {:timestamp 6351, :x-pos 150, :y-pos 122, :color nil} {:timestamp 6367, :x-pos 151, :y-pos 120, :color nil} {:timestamp 6379, :x-pos 151, :y-pos 119, :color nil} {:timestamp 6396, :x-pos 151, :y-pos 118, :color nil} {:timestamp 6413, :x-pos 151, :y-pos 117, :color nil} {:timestamp 6430, :x-pos 151, :y-pos 115, :color nil} {:timestamp 6446, :x-pos 151, :y-pos 114, :color nil} {:timestamp 6463, :x-pos 149, :y-pos 113, :color nil} {:timestamp 6481, :x-pos 147, :y-pos 113, :color nil} {:timestamp 6498, :x-pos 146, :y-pos 112, :color nil} {:timestamp 6527, :x-pos 145, :y-pos 112, :color nil}], :hash 3, :selected true}, 4 {:mouse-positions [{:timestamp 8350, :x-pos 169, :y-pos 345, :color nil} {:timestamp 8367, :x-pos 164, :y-pos 343, :color nil} {:timestamp 8385, :x-pos 154, :y-pos 334, :color nil} {:timestamp 8401, :x-pos 146, :y-pos 323, :color nil} {:timestamp 8417, :x-pos 135, :y-pos 312, :color nil} {:timestamp 8434, :x-pos 128, :y-pos 299, :color nil} {:timestamp 8451, :x-pos 120, :y-pos 288, :color nil} {:timestamp 8467, :x-pos 116, :y-pos 279, :color nil} {:timestamp 8485, :x-pos 113, :y-pos 268, :color nil} {:timestamp 8502, :x-pos 112, :y-pos 260, :color nil} {:timestamp 8518, :x-pos 112, :y-pos 250, :color nil} {:timestamp 8535, :x-pos 112, :y-pos 241, :color nil} {:timestamp 8551, :x-pos 112, :y-pos 234, :color nil} {:timestamp 8570, :x-pos 116, :y-pos 228, :color nil} {:timestamp 8586, :x-pos 120, :y-pos 221, :color nil} {:timestamp 8603, :x-pos 125, :y-pos 218, :color nil} {:timestamp 8619, :x-pos 128, :y-pos 215, :color nil} {:timestamp 8637, :x-pos 131, :y-pos 214, :color nil} {:timestamp 8652, :x-pos 133, :y-pos 213, :color nil} {:timestamp 8670, :x-pos 136, :y-pos 212, :color nil} {:timestamp 8687, :x-pos 139, :y-pos 212, :color nil} {:timestamp 8704, :x-pos 142, :y-pos 212, :color nil} {:timestamp 8722, :x-pos 147, :y-pos 212, :color nil} {:timestamp 8738, :x-pos 151, :y-pos 212, :color nil} {:timestamp 8754, :x-pos 158, :y-pos 216, :color nil} {:timestamp 8772, :x-pos 163, :y-pos 222, :color nil} {:timestamp 8789, :x-pos 166, :y-pos 228, :color nil} {:timestamp 8805, :x-pos 171, :y-pos 236, :color nil} {:timestamp 8837, :x-pos 173, :y-pos 243, :color nil} {:timestamp 8854, :x-pos 175, :y-pos 249, :color nil} {:timestamp 8871, :x-pos 176, :y-pos 257, :color nil} {:timestamp 8887, :x-pos 177, :y-pos 263, :color nil} {:timestamp 8905, :x-pos 177, :y-pos 269, :color nil} {:timestamp 8920, :x-pos 177, :y-pos 278, :color nil} {:timestamp 8938, :x-pos 177, :y-pos 286, :color nil} {:timestamp 8953, :x-pos 176, :y-pos 296, :color nil} {:timestamp 8970, :x-pos 173, :y-pos 307, :color nil} {:timestamp 8988, :x-pos 172, :y-pos 315, :color nil} {:timestamp 9002, :x-pos 168, :y-pos 326, :color nil} {:timestamp 9020, :x-pos 167, :y-pos 337, :color nil} {:timestamp 9037, :x-pos 166, :y-pos 344, :color nil} {:timestamp 9053, :x-pos 165, :y-pos 352, :color nil} {:timestamp 9070, :x-pos 164, :y-pos 359, :color nil} {:timestamp 9089, :x-pos 163, :y-pos 365, :color nil} {:timestamp 9103, :x-pos 161, :y-pos 372, :color nil} {:timestamp 9121, :x-pos 159, :y-pos 380, :color nil} {:timestamp 9137, :x-pos 157, :y-pos 385, :color nil} {:timestamp 9154, :x-pos 154, :y-pos 388, :color nil} {:timestamp 9170, :x-pos 151, :y-pos 390, :color nil} {:timestamp 9187, :x-pos 149, :y-pos 391, :color nil} {:timestamp 9203, :x-pos 146, :y-pos 392, :color nil} {:timestamp 9220, :x-pos 144, :y-pos 393, :color nil} {:timestamp 9238, :x-pos 142, :y-pos 393, :color nil} {:timestamp 9254, :x-pos 141, :y-pos 393, :color nil} {:timestamp 9271, :x-pos 139, :y-pos 393, :color nil} {:timestamp 9287, :x-pos 137, :y-pos 393, :color nil} {:timestamp 9305, :x-pos 136, :y-pos 393, :color nil} {:timestamp 9320, :x-pos 135, :y-pos 393, :color nil} {:timestamp 9338, :x-pos 134, :y-pos 391, :color nil} {:timestamp 9354, :x-pos 133, :y-pos 387, :color nil} {:timestamp 9371, :x-pos 132, :y-pos 382, :color nil} {:timestamp 9387, :x-pos 132, :y-pos 376, :color nil} {:timestamp 9405, :x-pos 132, :y-pos 372, :color nil} {:timestamp 9422, :x-pos 132, :y-pos 364, :color nil} {:timestamp 9438, :x-pos 133, :y-pos 357, :color nil} {:timestamp 9455, :x-pos 139, :y-pos 351, :color nil} {:timestamp 9472, :x-pos 144, :y-pos 344, :color nil} {:timestamp 9489, :x-pos 150, :y-pos 337, :color nil} {:timestamp 9506, :x-pos 158, :y-pos 331, :color nil} {:timestamp 9523, :x-pos 165, :y-pos 326, :color nil} {:timestamp 9541, :x-pos 171, :y-pos 321, :color nil} {:timestamp 9558, :x-pos 176, :y-pos 318, :color nil} {:timestamp 9574, :x-pos 180, :y-pos 316, :color nil} {:timestamp 9605, :x-pos 183, :y-pos 315, :color nil} {:timestamp 9623, :x-pos 185, :y-pos 315, :color nil} {:timestamp 9638, :x-pos 187, :y-pos 315, :color nil} {:timestamp 9656, :x-pos 190, :y-pos 315, :color nil} {:timestamp 9675, :x-pos 192, :y-pos 315, :color nil}], :hash 4, :selected true, :hover false}, 5 {:mouse-positions [{:timestamp 10407, :x-pos 218, :y-pos 288, :color nil} {:timestamp 10424, :x-pos 217, :y-pos 288, :color nil} {:timestamp 10441, :x-pos 216, :y-pos 288, :color nil} {:timestamp 10458, :x-pos 214, :y-pos 288, :color nil} {:timestamp 10475, :x-pos 213, :y-pos 288, :color nil} {:timestamp 10492, :x-pos 211, :y-pos 288, :color nil} {:timestamp 10511, :x-pos 208, :y-pos 288, :color nil} {:timestamp 10528, :x-pos 206, :y-pos 288, :color nil} {:timestamp 10559, :x-pos 204, :y-pos 289, :color nil} {:timestamp 10577, :x-pos 203, :y-pos 291, :color nil} {:timestamp 10592, :x-pos 201, :y-pos 294, :color nil} {:timestamp 10611, :x-pos 199, :y-pos 298, :color nil} {:timestamp 10625, :x-pos 197, :y-pos 301, :color nil} {:timestamp 10644, :x-pos 196, :y-pos 304, :color nil} {:timestamp 10662, :x-pos 195, :y-pos 306, :color nil} {:timestamp 10678, :x-pos 194, :y-pos 309, :color nil} {:timestamp 10694, :x-pos 194, :y-pos 311, :color nil} {:timestamp 10711, :x-pos 194, :y-pos 316, :color nil} {:timestamp 10727, :x-pos 194, :y-pos 319, :color nil} {:timestamp 10744, :x-pos 194, :y-pos 322, :color nil} {:timestamp 10760, :x-pos 197, :y-pos 325, :color nil} {:timestamp 10779, :x-pos 199, :y-pos 328, :color nil} {:timestamp 10794, :x-pos 202, :y-pos 330, :color nil} {:timestamp 10812, :x-pos 205, :y-pos 332, :color nil} {:timestamp 10828, :x-pos 208, :y-pos 333, :color nil} {:timestamp 10846, :x-pos 211, :y-pos 335, :color nil} {:timestamp 10864, :x-pos 215, :y-pos 336, :color nil} {:timestamp 10883, :x-pos 220, :y-pos 337, :color nil} {:timestamp 10902, :x-pos 225, :y-pos 338, :color nil} {:timestamp 10921, :x-pos 229, :y-pos 338, :color nil} {:timestamp 10940, :x-pos 233, :y-pos 338, :color nil} {:timestamp 10957, :x-pos 235, :y-pos 338, :color nil} {:timestamp 10975, :x-pos 238, :y-pos 335, :color nil} {:timestamp 10993, :x-pos 239, :y-pos 333, :color nil} {:timestamp 11010, :x-pos 240, :y-pos 327, :color nil} {:timestamp 11030, :x-pos 241, :y-pos 323, :color nil} {:timestamp 11046, :x-pos 241, :y-pos 318, :color nil} {:timestamp 11064, :x-pos 241, :y-pos 313, :color nil} {:timestamp 11081, :x-pos 241, :y-pos 309, :color nil} {:timestamp 11097, :x-pos 241, :y-pos 306, :color nil} {:timestamp 11117, :x-pos 239, :y-pos 302, :color nil} {:timestamp 11135, :x-pos 237, :y-pos 299, :color nil} {:timestamp 11156, :x-pos 235, :y-pos 296, :color nil} {:timestamp 11172, :x-pos 229, :y-pos 292, :color nil} {:timestamp 11189, :x-pos 227, :y-pos 290, :color nil} {:timestamp 11207, :x-pos 225, :y-pos 289, :color nil} {:timestamp 11223, :x-pos 223, :y-pos 288, :color nil} {:timestamp 11241, :x-pos 221, :y-pos 288, :color nil} {:timestamp 11258, :x-pos 218, :y-pos 288, :color nil} {:timestamp 11273, :x-pos 216, :y-pos 288, :color nil} {:timestamp 11291, :x-pos 214, :y-pos 289, :color nil} {:timestamp 11307, :x-pos 212, :y-pos 290, :color nil} {:timestamp 11325, :x-pos 211, :y-pos 293, :color nil} {:timestamp 11341, :x-pos 208, :y-pos 294, :color nil} {:timestamp 11360, :x-pos 207, :y-pos 296, :color nil} {:timestamp 11378, :x-pos 206, :y-pos 298, :color nil} {:timestamp 11394, :x-pos 206, :y-pos 299, :color nil} {:timestamp 11414, :x-pos 205, :y-pos 301, :color nil} {:timestamp 11432, :x-pos 205, :y-pos 302, :color nil} {:timestamp 11450, :x-pos 205, :y-pos 303, :color nil} {:timestamp 11466, :x-pos 205, :y-pos 304, :color nil} {:timestamp 11484, :x-pos 205, :y-pos 306, :color nil} {:timestamp 11503, :x-pos 206, :y-pos 306, :color nil} {:timestamp 11520, :x-pos 208, :y-pos 306, :color nil} {:timestamp 11539, :x-pos 212, :y-pos 306, :color nil} {:timestamp 11556, :x-pos 215, :y-pos 306, :color nil} {:timestamp 11574, :x-pos 219, :y-pos 306, :color nil} {:timestamp 11591, :x-pos 221, :y-pos 306, :color nil} {:timestamp 11611, :x-pos 223, :y-pos 306, :color nil} {:timestamp 11629, :x-pos 224, :y-pos 305, :color nil}], :hash 5, :selected true, :hover false}, 6 {:mouse-positions [{:timestamp 12415, :x-pos 227, :y-pos 328, :color nil} {:timestamp 12431, :x-pos 227, :y-pos 327, :color nil} {:timestamp 12449, :x-pos 227, :y-pos 324, :color nil} {:timestamp 12465, :x-pos 227, :y-pos 320, :color nil} {:timestamp 12481, :x-pos 228, :y-pos 319, :color nil} {:timestamp 12498, :x-pos 229, :y-pos 316, :color nil} {:timestamp 12517, :x-pos 231, :y-pos 313, :color nil} {:timestamp 12550, :x-pos 233, :y-pos 311, :color nil} {:timestamp 12567, :x-pos 235, :y-pos 310, :color nil} {:timestamp 12584, :x-pos 236, :y-pos 308, :color nil} {:timestamp 12602, :x-pos 238, :y-pos 305, :color nil} {:timestamp 12618, :x-pos 240, :y-pos 303, :color nil} {:timestamp 12635, :x-pos 242, :y-pos 301, :color nil} {:timestamp 12652, :x-pos 244, :y-pos 297, :color nil} {:timestamp 12670, :x-pos 246, :y-pos 295, :color nil} {:timestamp 12688, :x-pos 248, :y-pos 294, :color nil} {:timestamp 12706, :x-pos 250, :y-pos 294, :color nil} {:timestamp 12725, :x-pos 252, :y-pos 293, :color nil} {:timestamp 12743, :x-pos 254, :y-pos 293, :color nil} {:timestamp 12762, :x-pos 255, :y-pos 293, :color nil} {:timestamp 12782, :x-pos 257, :y-pos 293, :color nil} {:timestamp 12800, :x-pos 260, :y-pos 293, :color nil} {:timestamp 12823, :x-pos 260, :y-pos 294, :color nil} {:timestamp 12844, :x-pos 260, :y-pos 297, :color nil} {:timestamp 12864, :x-pos 260, :y-pos 302, :color nil} {:timestamp 12883, :x-pos 259, :y-pos 307, :color nil} {:timestamp 12904, :x-pos 253, :y-pos 316, :color nil} {:timestamp 12926, :x-pos 251, :y-pos 319, :color nil} {:timestamp 12946, :x-pos 249, :y-pos 320, :color nil} {:timestamp 12969, :x-pos 248, :y-pos 321, :color nil} {:timestamp 12991, :x-pos 246, :y-pos 323, :color nil} {:timestamp 13105, :x-pos 246, :y-pos 322, :color nil} {:timestamp 13123, :x-pos 249, :y-pos 318, :color nil} {:timestamp 13145, :x-pos 254, :y-pos 312, :color nil} {:timestamp 13163, :x-pos 260, :y-pos 304, :color nil} {:timestamp 13183, :x-pos 265, :y-pos 298, :color nil} {:timestamp 13201, :x-pos 269, :y-pos 293, :color nil} {:timestamp 13221, :x-pos 275, :y-pos 288, :color nil} {:timestamp 13239, :x-pos 282, :y-pos 284, :color nil} {:timestamp 13258, :x-pos 284, :y-pos 283, :color nil} {:timestamp 13277, :x-pos 286, :y-pos 283, :color nil} {:timestamp 13296, :x-pos 288, :y-pos 283, :color nil} {:timestamp 13314, :x-pos 290, :y-pos 283, :color nil} {:timestamp 13333, :x-pos 291, :y-pos 283, :color nil} {:timestamp 13352, :x-pos 292, :y-pos 283, :color nil} {:timestamp 13371, :x-pos 292, :y-pos 284, :color nil} {:timestamp 13389, :x-pos 292, :y-pos 287, :color nil} {:timestamp 13411, :x-pos 292, :y-pos 291, :color nil} {:timestamp 13430, :x-pos 291, :y-pos 296, :color nil} {:timestamp 13450, :x-pos 285, :y-pos 305, :color nil} {:timestamp 13471, :x-pos 283, :y-pos 310, :color nil} {:timestamp 13490, :x-pos 281, :y-pos 313, :color nil} {:timestamp 13512, :x-pos 279, :y-pos 315, :color nil} {:timestamp 13531, :x-pos 278, :y-pos 317, :color nil} {:timestamp 13552, :x-pos 277, :y-pos 319, :color nil} {:timestamp 13572, :x-pos 277, :y-pos 320, :color nil} {:timestamp 13592, :x-pos 277, :y-pos 321, :color nil} {:timestamp 13613, :x-pos 277, :y-pos 322, :color nil} {:timestamp 13632, :x-pos 277, :y-pos 323, :color nil} {:timestamp 13654, :x-pos 277, :y-pos 324, :color nil} {:timestamp 13675, :x-pos 278, :y-pos 324, :color nil} {:timestamp 13697, :x-pos 285, :y-pos 324, :color nil} {:timestamp 13717, :x-pos 291, :y-pos 324, :color nil} {:timestamp 13738, :x-pos 295, :y-pos 323, :color nil} {:timestamp 13759, :x-pos 300, :y-pos 319, :color nil} {:timestamp 13780, :x-pos 310, :y-pos 310, :color nil} {:timestamp 13801, :x-pos 314, :y-pos 305, :color nil} {:timestamp 13822, :x-pos 316, :y-pos 299, :color nil} {:timestamp 13841, :x-pos 319, :y-pos 291, :color nil} {:timestamp 13862, :x-pos 321, :y-pos 286, :color nil} {:timestamp 13885, :x-pos 324, :y-pos 275, :color nil} {:timestamp 13911, :x-pos 330, :y-pos 258, :color nil} {:timestamp 13931, :x-pos 334, :y-pos 249, :color nil} {:timestamp 13949, :x-pos 342, :y-pos 232, :color nil} {:timestamp 13969, :x-pos 344, :y-pos 225, :color nil} {:timestamp 13987, :x-pos 346, :y-pos 220, :color nil} {:timestamp 14008, :x-pos 347, :y-pos 217, :color nil} {:timestamp 14026, :x-pos 347, :y-pos 214, :color nil} {:timestamp 14046, :x-pos 347, :y-pos 212, :color nil} {:timestamp 14066, :x-pos 347, :y-pos 211, :color nil} {:timestamp 14102, :x-pos 346, :y-pos 211, :color nil} {:timestamp 14120, :x-pos 344, :y-pos 217, :color nil} {:timestamp 14137, :x-pos 343, :y-pos 223, :color nil} {:timestamp 14154, :x-pos 341, :y-pos 236, :color nil} {:timestamp 14171, :x-pos 339, :y-pos 243, :color nil} {:timestamp 14188, :x-pos 337, :y-pos 255, :color nil} {:timestamp 14207, :x-pos 335, :y-pos 265, :color nil} {:timestamp 14223, :x-pos 333, :y-pos 275, :color nil} {:timestamp 14241, :x-pos 331, :y-pos 285, :color nil} {:timestamp 14257, :x-pos 329, :y-pos 293, :color nil} {:timestamp 14275, :x-pos 328, :y-pos 299, :color nil} {:timestamp 14294, :x-pos 325, :y-pos 305, :color nil} {:timestamp 14314, :x-pos 323, :y-pos 308, :color nil} {:timestamp 14330, :x-pos 322, :y-pos 311, :color nil} {:timestamp 14349, :x-pos 321, :y-pos 312, :color nil} {:timestamp 14366, :x-pos 320, :y-pos 313, :color nil} {:timestamp 14382, :x-pos 319, :y-pos 314, :color nil} {:timestamp 14402, :x-pos 318, :y-pos 315, :color nil} {:timestamp 14419, :x-pos 318, :y-pos 316, :color nil} {:timestamp 14439, :x-pos 317, :y-pos 317, :color nil} {:timestamp 14458, :x-pos 317, :y-pos 318, :color nil} {:timestamp 14476, :x-pos 316, :y-pos 318, :color nil} {:timestamp 14531, :x-pos 316, :y-pos 317, :color nil} {:timestamp 14550, :x-pos 316, :y-pos 314, :color nil} {:timestamp 14571, :x-pos 320, :y-pos 309, :color nil} {:timestamp 14594, :x-pos 323, :y-pos 304, :color nil} {:timestamp 14614, :x-pos 327, :y-pos 299, :color nil} {:timestamp 14636, :x-pos 330, :y-pos 294, :color nil} {:timestamp 14656, :x-pos 333, :y-pos 292, :color nil} {:timestamp 14676, :x-pos 335, :y-pos 291, :color nil} {:timestamp 14697, :x-pos 336, :y-pos 290, :color nil} {:timestamp 14719, :x-pos 337, :y-pos 290, :color nil} {:timestamp 14742, :x-pos 339, :y-pos 290, :color nil} {:timestamp 14764, :x-pos 341, :y-pos 293, :color nil} {:timestamp 14788, :x-pos 342, :y-pos 295, :color nil} {:timestamp 14813, :x-pos 343, :y-pos 298, :color nil} {:timestamp 14835, :x-pos 343, :y-pos 299, :color nil} {:timestamp 14858, :x-pos 343, :y-pos 300, :color nil} {:timestamp 14882, :x-pos 344, :y-pos 302, :color nil} {:timestamp 14906, :x-pos 344, :y-pos 303, :color nil} {:timestamp 14931, :x-pos 344, :y-pos 304, :color nil} {:timestamp 14954, :x-pos 344, :y-pos 307, :color nil} {:timestamp 14978, :x-pos 344, :y-pos 309, :color nil} {:timestamp 15001, :x-pos 344, :y-pos 313, :color nil} {:timestamp 15023, :x-pos 344, :y-pos 314, :color nil} {:timestamp 15050, :x-pos 344, :y-pos 317, :color nil} {:timestamp 15072, :x-pos 346, :y-pos 321, :color nil} {:timestamp 15098, :x-pos 348, :y-pos 323, :color nil} {:timestamp 15121, :x-pos 355, :y-pos 328, :color nil} {:timestamp 15143, :x-pos 360, :y-pos 329, :color nil} {:timestamp 15167, :x-pos 363, :y-pos 331, :color nil} {:timestamp 15190, :x-pos 367, :y-pos 332, :color nil} {:timestamp 15212, :x-pos 368, :y-pos 332, :color nil} {:timestamp 15232, :x-pos 369, :y-pos 332, :color nil} {:timestamp 15255, :x-pos 371, :y-pos 326, :color nil} {:timestamp 15277, :x-pos 372, :y-pos 323, :color nil} {:timestamp 15297, :x-pos 373, :y-pos 319, :color nil} {:timestamp 15322, :x-pos 375, :y-pos 317, :color nil} {:timestamp 15344, :x-pos 376, :y-pos 310, :color nil} {:timestamp 15365, :x-pos 377, :y-pos 308, :color nil} {:timestamp 15388, :x-pos 378, :y-pos 302, :color nil} {:timestamp 15409, :x-pos 380, :y-pos 281, :color nil} {:timestamp 15430, :x-pos 380, :y-pos 274, :color nil}], :hash 6, :selected true, :hover false}}, :in-progress-line [], :current-millisecond 1400020762328}))
