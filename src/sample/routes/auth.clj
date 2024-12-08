(ns sample.routes.auth
  (:require [hiccup.page :refer [html5 include-css]]
            [compojure.core :refer [defroutes GET POST]]
            [sample.views.home :refer [home-page]]
            [sample.views.report :refer [report-page]]
            [sample.views.upload :refer [upload-page]]
            [ring.util.response :as response]
            [sample.models.user :as db]
             [sample.views.auth :as view]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn login-page [& [email errors]]
  (view/login-page email errors))

(defroutes auth-routes
  (GET "/login" [] (login-page)))

