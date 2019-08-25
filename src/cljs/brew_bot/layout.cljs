(ns brew-bot.layout
  (:require [antizer.reagent :as ant]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn user-form
  [display-buttons?]
  (fn [props]
    (let [form (ant/get-form)
          form-style {:label-col {:span 6}
                      :wrapper-col {:span 13}}]
        [ant/form {:layout "horizontal"}
          [ant/form-item (merge form-style {:label "Name"})
            (ant/decorate-field form "name" {:rules [{:required true}]}
              [ant/input])]
          [ant/form-item (merge form-style {:label "Email"})
            (ant/decorate-field form "email" {:rules [{:required true} {:type "email"}]}
              [ant/input])]
          (if display-buttons?
            [ant/form-item {:wrapper-col {:offset 6}}
              [ant/row
                [ant/col {:span 4}
                  [ant/button {:type "primary" :on-click #(ant/validate-fields form)}
                    "Submit"]]
                [ant/col {:offset 1}
                  [ant/button {:on-click #(ant/reset-fields form)}
                    "Reset"]]]])])))

(defn form-example
  []
  [:div {:style {:padding-left "10px"}}
    [:h2 "Form"]
    (ant/create-form (user-form true))])

(defn render-full-row
  [example]
  [ant/col {:span 24}
    [:div.box {:key (random-uuid)}
      [:div.box-content
        [example]]]])

(defn content-area
  []
  [ant/layout-content {:class "content-area"}
    [ant/row {:gutter 12}
      (render-full-row form-example)]])

(defn side-menu
  []
  [ant/menu {:mode "inline" :theme "dark" :style {:height "100%"}}
    [ant/menu-item {:on-click #(rf/dispatch [:update-current-page :about])} "Learn About Me"]
    [ant/menu-sub-menu {:title "Generate Recipes"}
      [ant/menu-item {:on-click #(rf/dispatch [:update-current-page :random-recipe])}             "Purely Random"]
      [ant/menu-item {:on-click #(rf/dispatch [:update-current-page :constrained-random-recipe])} "Constrained Random"]
      [ant/menu-item {:on-click #(rf/dispatch [:update-current-page :weighted-random-recipe])}    "Weighted Random"]]])

(defn main-panel
  []
  (let [current-page @(rf/subscribe [:current-page])]
    (fn []
      [ant/locale-provider {:locale (ant/locales "en_US")}
      [ant/layout {:style {:height "100%" :width "100%"}}
        [ant/affix
          [ant/layout-header {:class "banner"}
            (r/as-element
              [ant/row
                [ant/col {:span 12}
                  [:h2 {:style {:color "white" :margin-bottom "-12px"}} "Brew Bot"]]
                [ant/col {:span 1 :offset 11}
                  [:a {:href "https://github.com/nnichols/brew-bot"}
                    [ant/icon {:class "banner-logo" :type "github"}]]]])]]
        [ant/layout
          [ant/layout-sider [side-menu]]
          [ant/layout {:style {:width "60%"}}
            [content-area]]]]])))
