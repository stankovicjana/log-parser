(ns sample.handler
  (:require
   [cheshire.core :as json]
   [compojure.core :refer [defroutes GET POST routes]]
   [migratus.core :as migratus]
   [ring.middleware.multipart-params :refer [wrap-multipart-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.util.response :refer [response]]
   [sample.parser :refer [process-logfile]]
   [sample.routes.auth :refer [auth-routes]]
   [sample.views.home :as view]
   [sample.views.report :refer [report-page]]
   [sample.views.upload :refer [upload-page]]))

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
      (wrap-params)
      (wrap-resource "public")
      (wrap-multipart-params)))
