(ns brew-bot.layout
  (:require [antizer.reagent :as ant]
            [brew-bot.recipe-generation.views :as recipe-generator]
            [brew-bot.recipes.views :as recipes]
            [brew-bot.text-pages.static-content :as static]
            [brew-bot.visual-identity :as vi]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn app-banner
  []
  [ant/layout-header {:class "banner"}
   (r/as-element
    [ant/row
     [ant/col {:span 11}
      [:h2 {:on-click #(rf/dispatch [:navigate "/"])
            :style {:color "white" :margin-bottom "-12px" :cursor "pointer"}} "Brew Bot (beta)"]]
     [ant/col {:span 1 :offset 11}
      [:a {:href "https://github.com/nnichols/brew-bot" :target "_blank"}
       [ant/icon {:class "banner-logo" :type "github" :title "Visit me on GitHub"}]]]])])

(defn app-footer
  []
  [ant/layout-footer {:style {:background-color vi/dark-blue}}
   (r/as-element
    [ant/row {:style {:align "bottom" :text-align "center" :color vi/dim-gray}}
     [ant/col
      [:p {:style {:font-size "10pt"}}
       [:a {:href "https://github.com/nnichols/brew-bot/issues" :target "_blank"}
        "Found something broken? Open an issue!"]]]])])

(defn side-menu
  [has-recipe-changed?]
  [ant/menu {:mode "inline" :theme "dark"}
   [ant/menu-sub-menu {:title "How I Work"}
    [ant/menu-item {:on-click #(rf/dispatch [:navigate "/about/random"])} "Random Recipes"]
    [ant/menu-item {:on-click #(rf/dispatch [:navigate "/about/weighted"])} "Weighted Recipes"]]
   [ant/menu-sub-menu {:title "Generate Recipes"}
    [ant/menu-item {:on-click #(rf/dispatch [:navigate "/generators/random"])}          "Purely Random"]
    [ant/menu-item {:on-click #(rf/dispatch [:navigate "/generators/limited-random"])}  "Constrained Random"]
    [ant/menu-item {:on-click #(rf/dispatch [:navigate "/generators/weighted-random"])} "Weighted Random"]
    [ant/menu-item {:on-click #(rf/dispatch [:navigate "/generators/weighted-guided"])} "Weighted Guided"]]
   [ant/menu-item {:on-click #(rf/dispatch [:navigate "/about-me"])} "Learn About Me"]
   [ant/menu-item {:on-click #(rf/dispatch [:navigate "/contributors"])} "Contributors"]
   [ant/menu-sub-menu {:title "External Links"}
     [ant/menu-item [:a {:href "https://wallbrew.com" :target "_blank"} "Wall Brewing Co."]]
     [ant/menu-item [:a {:href "https://nnichols.github.io" :target "_blank"} "GitHub Page"]]]])

(defn main-body
  [current-page has-recipe-changed?]
  (fn [current-page has-recipe-changed?]
    (let [recipe-page? (#{:random :limited-random :weighted-random :weighted-guided} current-page)]
      [ant/layout {:style {:width "60%"}}
       [ant/layout-content {:class "content-area"}
        [ant/row {:gutter 12 :style {:padding-left "20px" :padding-top "20px"}}
         (cond
           (= :about current-page)                 [static/about-me]
           (= :home current-page)                  [static/home-page]
           (= :random-generators current-page)     [static/random-generators]
           (= :weighted-generators current-page)   [static/weighted-generators]
           (= :contributors current-page)          [static/contributors]
           recipe-page?                            [recipe-generator/recipe-generator-body current-page has-recipe-changed?]
           (= :recipe-preview current-page)        [recipes/recipe-display-page]
           :else                                   [static/not-found])]]])))

(defn main-panel
  []
  (let [current-page (rf/subscribe [:current-page])
        has-recipe-changed? (rf/subscribe [:has-recipe-changed?])]
    (fn []
      (let [recipe-page? (#{:random :limited-random :weighted-random :weighted-guided} @current-page)]
        [ant/locale-provider {:locale (ant/locales "en_US")}
         [ant/layout {:style {:min-height "100vh" :min-width "800px"}}
          [ant/affix [app-banner]]
          [ant/layout
           [ant/layout-sider [side-menu @has-recipe-changed?]]
           [main-body @current-page @has-recipe-changed?]]
          [app-footer]]]))))
