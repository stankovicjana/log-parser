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
   [sample.views.upload :refer [upload-page]]
   [clojure.data.json :as jsonData]))

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


(defn get-logs [log-type start-date end-date]
  (cond
    (= log-type "event-viewer") {:logs [{:level "Error" :dateTime "2024-12-11 10:00 AM" :source "System" :eventId 1 :taskCategory "System Crash"}]}
    (= log-type "system-log") {:logs [{:level "Warning" :dateTime "2024-12-11 11:30 AM" :source "Security" :eventId 2 :taskCategory "Login Attempt"}]}
    (= log-type "application-log") {:logs [{:level "Info" :dateTime "2024-12-11 02:00 PM" :source "App" :eventId 3 :taskCategory "Request"}]}
    (= log-type "security-log") {:logs [{:level "Alert" :dateTime "2024-12-11 03:15 PM" :source "Security" :eventId 4 :taskCategory "Suspicious Activity"}]}
    :else {:logs []}))

(defn trace-logs-handler [request]
  (let [params (jsonData/read-str (slurp (:body request)) :key-fn keyword)
        log-type (get params :logType)
        start-date (get params :startDate)
        end-date (get params :endDate)
        logs (get-logs log-type start-date end-date)]
    (response/response (json/generate-string logs))))

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
  (POST "/trace-logs" request (trace-logs-handler request))
  auth-routes
  home-routes
  private-routes)

(def app
  (-> (routes app-routes)
      (wrap-params)
      (wrap-session)
      (wrap-resource "public")
      (wrap-multipart-params)))
