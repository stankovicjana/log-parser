(ns sample.views.home
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer [form-to submit-button]]
            [sample.helpers :refer [input-control]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn home-page [& [email errors]]
  (html5
   [:head
    [:title "Home Page"]
    (include-css "/css/style.css")]
   [:body
    [:nav
     [:ul
      [:li (link-to "/" "Home")]
      [:li (link-to "/login" "Login")]
      [:li (link-to "/register" "Register")]]]
    [:div {:class "container"}
     [:div {:class "sidebar"}
      [:ul
       [:li (link-to "/upload" "Upload file")]
       [:li (link-to "/report" "Generate report")]]]
     [:div {:class "content"}
      [:div.login-form
       [:h1 "Login for more options"]
       ]]]]))

(defn registration-page [& [name email errors]]
  [:div.registration-form
   [:h1 "Let's create an account"]])
