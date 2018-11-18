(ns simanneal.tsp)

;;
;; Traveling Salesman Problem
;;

(defn random-point
  "Random 2D point vector"
  []
  [(rand) (rand)])

(defn switch-elements
  "Switch 2 elements in a vector by index"
  [v i1 i2]
  (assoc v i2 (v i1) i1 (v i2)))

(defn scale-point [[x y] scalex scaley]
  [(* x scalex) (* y scaley)])

(defn make-random-points [n]
  (into [] (map #(scale-point % 560 360) (take n (repeatedly random-point)))))

(defn distance
  "Take two 2D point vectors and calculate euclidean distance"
  [[x1 y1] [x2 y2]]
  (let [dx (- x2 x1)
        dy (- y2 y1)]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

(defn rand-bool
  "Return true or false with a percent chance of being true.
  percent defaults to 50.0
  FRom https://github.com/sjl/roul/blob/master/src/roul/random.clj
  "
  ([]
   (rand-bool 50.0))
  ([percent]
   (< (rand 100) percent)))

;; 1. Objective Function
(defn total-distance
  "Calculate the total distance of the tour.
  Lowest score is best; minimize"
  [tour]
  (reduce
   + (for [[pt1 pt2] (partition 2 1 tour)]
       (distance pt1 pt2))))

;; 2. Neighbor function

(defn swap-points-random
  "Given an ordered vector of points, randomly swap two random points."
  [pts]
  (let [n-pts (count pts)
        idx (rand-int n-pts)
        idx2 (rand-int n-pts)]
    (switch-elements pts idx idx2)))

(defn swap-points-kopt
  "Given an ordered vector of points, randomly swap two random points."
  [pts]
  (let [n-pts (count pts)
        idxa1 (rand-int (- n-pts 1))
        idxa2 (+ idxa1 1)
        idxb1 (rand-int (- n-pts 1))
        idxb2 (+ idxa1 1)]
    (->
      (switch-elements pts idxa1 idxa2)
      (switch-elements idxb1 idxb2))
    ))

(defn swap-points-neighbor
  "Given an ordered vector of points,
   randomly swap two adjacent points."
  [pts]
  (let [gap 1 
        idx (rand-int (- (count pts) gap))
        idx2 (+ gap idx)]
    (switch-elements pts idx idx2)))

(defn swap-points-hacks
  "Given an ordered vector of points,
   randomly swap two adjacent points."
  [pts]
  (let [gap (rand-int (int (/ (count pts) 2))) ;; hacks
        idx (rand-int (- (count pts) gap))
        idx2 (+ gap idx)]
    (switch-elements pts idx idx2)))

(def swap-functions (list
                    swap-points-random
                    ; swap-points-hacks
                    swap-points-neighbor
                    swap-points-kopt))

(defn swap-points [pts]
  ;; randomly select which swap-function to apply
  (let [n (count swap-functions)
        i (rand-int n)
        func (nth swap-functions i)]
    (func pts)))
