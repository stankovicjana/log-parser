(ns sample.views.auth
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer :all]
            [sample.models.user :as user-db]
            [ring.util.codec :refer [url-encode]]
            [hiccup.form :refer :all]
            [hiccup.element :refer :all]
            [hiccup.form :refer [label]]
            [hiccup.form :refer :all]
            [sample.models.user :as db]
            [sample.helpers :refer :all]
            [struct.core :as st]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer [form-to submit-button]]
            [sample.helpers :refer [input-control]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.response :refer [response]]))

(defn login-page [& [email errors]]
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
     
     [:div {:class "content"}
      [:div.login-form
       [:h1 "Login with existing account"]
       (form-to [:post "/login"]
                (anti-forgery-field)
                (input-control :text "email" "Email" email true (:email errors))
                (input-control :password "password" "Password" nil true)
                (submit-button {:class "btn btn-success"} "Login"))]]]]))

(defn registration-page [& [name email errors]]
  (response
   (html5
    [:head
     [:title "Registration Page"]
     (include-css "/css/style.css")]
    [:body
     [:div.registration-form
      [:h1 "Create an account"]
      ]])))
