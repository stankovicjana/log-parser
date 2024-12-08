(ns sample.views.home
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer [form-to submit-button]]
            )) 

(defn home-page []
  (html5
   [:head
    [:title "My Profile"]
    (include-css "/css/style.css")]
   [:body
    [:nav
     [:ul
      [:li (link-to "/" "My profile")]
      [:li (link-to "/login" "Login")]
      [:li (link-to "/register" "Register")]]]
    [:div {:class "container"}
     [:div {:class "sidebar"}
      [:ul
       [:li (link-to "/upload" "Upload file")]
       [:li (link-to "/report" "Generate report")]
       ]] 
     [:div {:class "content"}
      [:h2 "My profile"]]]]))

(defn login-page [& [email errors]]
  [:div.login-form
   [:h1 "Login with existing account"]])

(defn registration-page [& [name email errors]]
  [:div.registration-form
   [:h1 "Let's create an account"]
   ])
