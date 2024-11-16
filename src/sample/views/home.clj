(ns sample.views.home
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]))

(defn home-page []
  (html5
   [:head
    [:title "My Profile"]
    (include-css "/css/style.css")]
   [:body
    [:nav
     [:ul
      [:li (link-to "/" "My profile")]
      [:li (link-to "/" "Logout")]]]
    [:div {:class "container"}
     [:div {:class "sidebar"}
      [:ul
       [:li (link-to "/upload" "Upload file")]
       [:li (link-to "/report" "Generate report")]]]
     [:div {:class "content"}
      [:h2 "My profile"]]]]))
