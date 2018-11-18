(ns simanneal.events
  (:use-macros [cljs.core.async.macros :only [go]])
  (:require
    [re-frame.core :as re-frame]
    [simanneal.db :as db]
    [simanneal.tsp :as tsp]
    [simanneal.anneal :as sa]
    [cljs.core.async :refer [<! timeout]]
    [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-fx
 ::play-next-step
 (fn [cofx _]
   (let [db (:db cofx)
         {:keys [points step step-size temp-seq temp-params]} db
         max-step (int (- (/ (:iterations temp-params) step-size) 1))
         temps (subvec temp-seq 
                 (* step step-size) 
                 (* (+ 1 step) step-size))
         new-pts (sa/optimize
                   points             ;; initial state
                   tsp/swap-points    ;; move function
                   tsp/total-distance ;; score function
                   temps)]            ;; temperature sequence
     {:db (assoc db
                 :points new-pts
                 :energy (tsp/total-distance new-pts)
                 :step (+ 1 step))
      :dispatch-later [{:ms 2.00001                             ;; required for animation frame redraw
                        :dispatch [(if (= step max-step)
                                     ::reset
                                     (if (and
                                           (:running db)
                                           (< step max-step))
                                      ;; fire recursive event
                                      ::play-next-step))]}]})))

(re-frame/reg-event-fx
 ::update-temp-seq
 (fn [cofx [_ new-params]]
   (let [db (:db cofx)
         params (merge (:temp-params db) new-params)]
     {:db (assoc db :temp-params params)})))

(re-frame/reg-event-fx
 ::start-sa
 (fn [cofx _]
   (let [db (:db cofx)
         {:keys [max-temp min-temp iterations]} (:temp-params db)]
    {:db (assoc db
                :temp-seq (into [] (sa/make-temperature-seq max-temp min-temp iterations))
                :running true)
     :dispatch [::play-next-step]})))

(re-frame/reg-event-db
 ::pause
 (fn [db _]
   (assoc db :running false)))

(re-frame/reg-event-db
 ::reset
 (fn [current-db _]
   (assoc current-db
          :step 0
          :running false)))

(re-frame/reg-event-db
 ::randomize-points
 (fn [current-db _]
   (assoc current-db
           :points (tsp/make-random-points (:n-points current-db))
           :step nil)))

(re-frame/reg-event-db
 ::randomize-tour
 (fn [current-db _]
   (let [pts (:points current-db)]
     (assoc current-db
             :points (tsp/swap-points pts)
             :step 0))))
