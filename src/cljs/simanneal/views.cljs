(ns simanneal.views
  (:require
   [re-frame.core :as rf]
   [simanneal.events :as events]
   [simanneal.subs :as subs]))

(defn control-tsp []
  [:div {:class "box"}
   [:h2 "Traveling Salesman"]
   [:br]
   [:div {:class "buttons is-right"}
    [:button {:class "button"
              :on-click #(rf/dispatch [::events/randomize-points])} "New Tour"]
    ]])


(defn control-anneal []
  (let [{:keys [max-temp min-temp iterations]} @(rf/subscribe [::subs/temp-params])]
    [:div {:class "box"}
     [:h2 "Simulated Annealing"]
     [:br]
     [:div {:class "field is-horizontal"}
      [:div {:class "field-label is-small"}
       [:label {:class "label"} "T"
        [:sub "min"]]]
      [:div {:class "field-body"}
       [:div {:class "field"}
        [:div {:class "control"}
         [:input {:class "input is-small" :type "text" :value min-temp 
                  :on-change #(rf/dispatch [::events/update-temp-seq {:min-temp (-> % .-target .-value)}])
                  :placeholder "Minimum Temperature"}]]]]]
     [:div {:class "field is-horizontal"}
      [:div {:class "field-label is-small"}
       [:label {:class "label"} "T"
        [:sub "max"]]]
      [:div {:class "field-body"}
       [:div {:class "field"}
        [:div {:class "control"}
         [:input {:class "input is-small" :type "text" :value max-temp
                  :on-change #(rf/dispatch [::events/update-temp-seq {:max-temp (-> % .-target .-value)}])
                  :placeholder "Maximum Temperature"}]]]]]
     [:div {:class "field is-horizontal"}
      [:div {:class "field-label is-small"}
       [:label {:class "label"} "Iterations"]]
      [:div {:class "field-body"}
       [:div {:class "field"}
        [:div {:class "control"}
         [:input {:class "input is-small" :type "text" :value iterations 
                  :on-change #(rf/dispatch [::events/update-temp-seq {:iterations (-> % .-target .-value)}])
                  :placeholder "Number of iterations"}]]]]]

     [:div {:class "buttons is-right"}
      ; [:button {:class "button"} "Estimate"]
      [:button {:class "button"
                :disabled @(rf/subscribe [::subs/running])
                :on-click #(rf/dispatch [::events/randomize-tour])} "debug"]
      [:button {:class "button"
                :disabled (not @(rf/subscribe [::subs/running]))
                :on-click #(rf/dispatch [::events/pause])} "Pause"]
      [:button {:class "button"
                :disabled @(rf/subscribe [::subs/running])
                :on-click #(rf/dispatch [::events/start-sa])} "Run"]
      ]]))

(defn construct-line
  "Given a vector of 2D point vectors, encode the tour visiting all points as SVG.
   Returns an SVG path as a hiccup data structure."
  [[pt1 & pts]]
  [:path
   {:class "line"
    :d (str
        "M" (first pt1) "," (second pt1)
        (apply str (map #(str "L" (first %)   "," (second %)) pts))
        "L" (first pt1) "," (second pt1))}])

(def svg-width 560)
(def svg-height 360)

(defn render-points [points]
  [:svg {:width svg-width, :height svg-height}
   [:g {:transform "scale(1)"}
    (construct-line points)
    (for [[x y] points]
      ^{:key [x y]}  ;; reagant asks for key metadata, citing React performance
      [:circle {:r "2.01" :cx (str x) :cy (str y)}])]])

;; ---------------------------------------;;
(defn sanitize [name]
  (clojure.string/lower-case
   (clojure.string/replace name #" " "-")))

(defn display-energy []
  [:div {:class "columns chart-row"}
   [:div {:class "column chart-column is-one-third"}
    [:div {:class "chart-row-heading"} "Distance"]]
   [:div {:class "column chart-column"}
    [:div {:class "chart-row"} (int @(rf/subscribe [::subs/energy]))]]])

(defn display-steps []
  (let [{:keys [step max-step]} @(rf/subscribe [::subs/step])
        progress (* 100 (/ step max-step))]
    [:div {:class "columns chart-row"}
     [:div {:class "column chart-column is-one-third"}
      [:div {:class "chart-row-heading"} "Progress"]]
     [:div {:class "column chart-column"}
      [:div {:class "chart-row"} (if (> progress 0) (str (int progress) "%") [:br])]]]))

(defn display-temp []
  (let [{:keys [temp-seq step-size step max-step]} @(rf/subscribe [::subs/step])
        temperature (get temp-seq (* step step-size))]
    [:div {:class "columns chart-row"}
     [:div {:class "column chart-column is-one-third"}
      [:div {:class "chart-row-heading"} "Temperature"]]
     [:div {:class "column chart-column"}
      [:div {:class "chart-row"} (str (int temperature))]]]))

(defn main-panel []
  [:div

   [:div {:class "columns"}
    [:div {:class "column is-one-third"}
     (control-tsp)
     (control-anneal)]
    [:div {:class "column"}
     (render-points @(rf/subscribe [::subs/points]))]]

    ;; TODO diff betwee subscribe in main-panel vs func?
    (display-steps)
    (display-energy)
    (display-temp)
    ])
