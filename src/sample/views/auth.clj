(ns sample.views.auth
  (:require
   [hiccup.element :refer :all]
   [hiccup.element :refer [link-to]]
   [hiccup.form :refer :all]
   [hiccup.form :refer [form-to password-field submit-button text-field]]
   [sample.helpers :refer :all]
   [sample.views.layout :refer [common]]))

(defn login-page [& [email errors]]
   [:div.login-form
    [:h1 "Login with existing account"]
    (form-to [:post "/login"]
             (input-control text-field "email" "Email" email true (:email errors))
             (input-control password-field "password" "Password" nil true)
             (submit-button {:class "btn btn-success"} "Login"))])

(defn registration-page [& [name email errors]]
  [:div.registration-form
   [:h1 "Let's create an account"]
  ])
