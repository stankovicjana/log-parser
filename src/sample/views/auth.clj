(ns sample.views.auth
  (:require
   [hiccup.core :refer [html]]
   [hiccup.element :refer :all]
   [hiccup.element :refer [link-to]]
   [hiccup.form :refer :all]
   [hiccup.form :refer [form-to password-field submit-button text-field]]
   [sample.helpers :refer :all]))

(defn login-page [& [email errors]]
   [:div.login-form
    [:h1 "Login with existing account"]
    (form-to [:post "/login"]
             (input-control text-field "email" "Email" email true (:email errors))
             (input-control password-field "password" "Password" nil true)
             (submit-button {:class "btn btn-success"} "Login"))])

(defn registration-page [& [name email errors]]
  (html
   [:div.registration-form
    [:h1 "Create an account"]
    (form-to [:post "/register"]
             (input-control text-field "name" "Name" name true (:name errors))
             (input-control text-field "email" "Email" email true (:email errors))
             (input-control password-field "password" "Password" nil true (:password errors))
             (input-control password-field "password-confirmation" "Repeat password" nil true (:password-confirmation errors))
             (submit-button {:class "btn btn-success"} "Create account"))]))