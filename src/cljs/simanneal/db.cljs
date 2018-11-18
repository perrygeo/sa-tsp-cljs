(ns simanneal.db 
  (:require
    [simanneal.anneal :as sa]
    [simanneal.tsp :as tsp]))

(def step-size 350)
(def iterations 35000)
(def n-points 15)
(def min-temp 3.0)
(def max-temp 1200)
(def temps (into [] (sa/make-temperature-seq max-temp min-temp iterations)))

(def default-db
  {:temp-params {:min-temp min-temp
                 :max-temp max-temp
                 :iterations iterations }
   :temp-seq temps 
   :step 0
   :energy 0
   :running false
   :n-points n-points
   :points (tsp/make-random-points n-points)

   ;; internal iterations of the SA algorithm per animation step
   :step-size step-size
   })
