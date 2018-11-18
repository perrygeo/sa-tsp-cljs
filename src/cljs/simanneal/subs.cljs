(ns simanneal.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::points
 (fn [db]
   (:points db)))

(re-frame/reg-sub
 ::energy
 (fn [db]
   (:energy db)))

(re-frame/reg-sub
 ::temp-params
 (fn [db]
   (:temp-params db)))

(re-frame/reg-sub
 ::running
 (fn [db]
   (:running db)))

(re-frame/reg-sub
 ::step
 (fn [db]
   (let [{:keys [step-size temp-params]} db]
     ;; TODO duplicate logic in ::events/play-next-step
     {:max-step (int (- (/ (:iterations temp-params) step-size) 1))
      :step-size step-size
      :temp-seq (:temp-seq db)
      :step (:step db)})))
