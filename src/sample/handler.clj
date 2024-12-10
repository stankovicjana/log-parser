(ns sample.handler
  (:require
   [cheshire.core :as json]
   [clojure.tools.trace :as trace]
   [compojure.core :refer [defroutes GET POST routes context]]
   [ring.middleware.multipart-params :refer [wrap-multipart-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.util.response :as response]
   [sample.helpers :refer [get-user]]
   [sample.parser :refer [process-logfile]]
   [sample.routes.auth :refer [auth-routes login-page]]
   [sample.routes.home :refer [home-routes]]
   [sample.views.layout :as layout]
   [sample.views.trace :as view]
   [sample.views.upload :refer [upload-page]]))

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

(defn wrap-current-user-id [handler]
  (fn [request]
    (let [user-id (:user-id (:session request))]
      (handler (assoc request :user-id user-id)))))

(defroutes private-routes
  (wrap-current-user-id
   (context "" {:keys [user-id]}
     (GET "/upload" []
       (if user-id
         (upload (get-user user-id))
         (response/redirect "/login")))
     (GET "/trace" []
       (if user-id
         (trace (get-user user-id))
         (response/redirect "/login"))))))

(defroutes app-routes
  (POST "/upload" request
    (upload-file-handler request))
  auth-routes
  home-routes
  private-routes)

(def app
  (-> (routes app-routes)
      (wrap-params)
      (wrap-session)
      (wrap-resource "public")
      (wrap-multipart-params)))
