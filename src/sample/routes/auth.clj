(ns sample.routes.auth
  (:require [hiccup.form :refer :all]
             [compojure.core :refer :all]
             [postal.core :refer [send-message]]
             [ring.util.response :as response]
             [sample.crypt :as crypt]
             [sample.models.user :as db]
             [sample.helpers :refer :all]
             [sample.views.layout :as layout]
             [sample.views.auth :as view]
             [struct.core :as st]))

(defn login-page [& [email errors]]
  (layout/common
   (view/login-page email errors)))

(defn user-to-session [user]
  {:user-id (:id user)
   :user-name (:name user)
   :user-email (:email user)})

(defn handle-login [email password]
  (println "Received login request with email:" email "and password:" password)
  (let [user (db/get-user-by-email email)]
    (if (and user (crypt/verify password (:encrypted_password user)))
      (response/redirect "/")
      (login-page email {:email "Email or password is invalid"}))))

(defroutes auth-routes
  (GET "/login" [] (login-page))
  (POST "/login" [email password]
    (handle-login email password))
   (GET "/users" []
     (response/response (db/get-all-users))))