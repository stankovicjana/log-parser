(ns sample.routes.auth
  (:require [hiccup.form :refer :all]
             [compojure.core :refer :all]
             [postal.core :refer [send-message]]
             [ring.util.response :refer [response]]
             [sample.models.user :as db]
             [sample.views.auth :as view]))
  
  (defn login-page [& [email errors]]                      
    (response (view/login-page email errors)))
  
  (defn registration-page [& [name email errors]]                    
    (response (view/registration-page name email errors)))
  
  (defroutes auth-routes
    (GET "/login" []
      (login-page))
    (GET "/register" []
      (registration-page))
    )
                     