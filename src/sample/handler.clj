(ns sample.handler
  (:require [compojure.core :refer [defroutes GET POST routes]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.util.response :refer [response]]
            [ring.middleware.resource :refer [wrap-resource]]
            [sample.parser :refer [process-logfile]]
            [sample.views.home :as view]
            [sample.views.report :refer [report-page]]
            [sample.views.upload :refer [upload-page]]
            [sample.routes.auth :refer [auth-routes]]
            [cheshire.core :as json]
            [migratus.core :as migratus]))

(def migratus-config
  {:store :database
   :migration-dir "migrations"
   :db (or (System/getenv "DATABASE_PARSER") "postgresql://localhost:5432/parser")})

(defn init []
  (migratus/migrate migratus-config))

(defn upload-file-handler [request]
  (let [file (get-in request [:params "file"])]
    (if (some? file)
      (let [filename (:tempfile file)
            logs (process-logfile filename)]
        (response (json/generate-string logs)))
      (response {:error "No file provided"}))))

(defn home-page [& [email errors]]
    (view/home-page email errors))  

(defroutes app-routes
  (GET "/" [] (home-page))
  (GET "/report" [] (report-page))
  (GET "/upload" [] (upload-page))
  (POST "/uploadFile" request (upload-file-handler request)))

(def app
  (-> (routes app-routes auth-routes)
      (wrap-resource "public")
      (wrap-multipart-params)))
