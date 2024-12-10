(ns sample.handler
  (:require
   [cheshire.core :as json]
   [clojure.tools.trace :as trace]
   [compojure.core :refer [defroutes GET POST routes]]
   [migratus.core :as migratus]
   [ring.middleware.multipart-params :refer [wrap-multipart-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.util.response :as response]
   [sample.helpers :refer [get-user]]
   [sample.middleware.auth :refer [wrap-authentication]]
   [sample.parser :refer [process-logfile]]
   [sample.routes.auth :refer [auth-routes]]
   [sample.routes.home :refer [home-routes]]
   [sample.views.layout :as layout]
   [sample.views.trace :as view]
   [sample.views.upload :refer [upload-page]]))

(def migratus-config
  {:store :database
   :migration-dir "migrations"
   :db (or (System/getenv "DATABASE_PARSER") "postgresql://localhost:5432/parser")})

(defn init []
  (migratus/migrate migratus-config))


(defn upload-file-handler [request]
  (trace/trace "Request params:" (get-in request [:params "file"]))  
  (let [file (get-in request [:params "file"])]
    (if file
      (let [filename (:tempfile file)
            logs (process-logfile filename)]
        (trace/trace "Processed logs:" logs)
        (response/response (json/generate-string logs)))
    (response/response (json/generate-string [])))))

(defn upload [user]
  (layout/common (upload-page user) user))

(defn trace [user]
  (layout/common (view/trace-page user) user))

(defroutes app-routes
  (GET "/trace" {{:keys [user-id]} :session}
                 (trace (get-user user-id)))
  (GET "/upload" {{:keys [user-id]} :session}
  (upload (get-user user-id)))  
  (POST "/upload" request (upload-file-handler request)) 
  auth-routes
  home-routes)

(def app
  (-> (routes app-routes)
      (wrap-params)
      (wrap-session)
      (wrap-authentication)
      (wrap-resource "public")
      (wrap-multipart-params)))
