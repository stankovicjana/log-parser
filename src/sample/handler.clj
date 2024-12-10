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
   [ring.util.response :refer [response]]
   [sample.parser :refer [process-logfile]]
   [sample.routes.auth :refer [auth-routes]]
   [sample.routes.home :refer [home-routes]]
   [sample.views.report :refer [report-page]]
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
        (response (json/generate-string logs)))
      (response (json/generate-string [])))))

(defroutes app-routes
  (GET "/report" [] (report-page))
  (GET "/upload" [] (upload-page))
  (POST "/upload" request (upload-file-handler request))
  auth-routes
  home-routes)

(def app
  (-> (routes app-routes)
      (wrap-params)
      (wrap-session)
      (wrap-resource "public")
      (wrap-multipart-params)))
