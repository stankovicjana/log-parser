(ns sample.handler
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log]
   [clojure.tools.trace :as trace]
   [compojure.core :refer [context defroutes GET POST routes]]
   [org.httpkit.server :refer [on-close on-receive send! with-channel]]
   [postal.core :as postal]
   [ring.middleware.cors :refer [wrap-cors]]
   [ring.middleware.multipart-params :refer [wrap-multipart-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.util.response :as response]
   [sample.helpers :refer [get-user]]
   [sample.helpers :refer [send-email-with-attachment]]
   [sample.logs :refer [watch-file]]
   [sample.logs :refer [watch-file]]
   [sample.models.friends :refer [add-friend fetch-friends]]
   [sample.parser :refer [process-logfile]]
   [sample.routes.auth :refer [auth-routes]]
   [sample.routes.friends :refer [friends-routes]]
   [sample.routes.home :as home]
   [sample.views.layout :as layout]
   [sample.views.trace :as view]
   [sample.views.upload :refer [upload-page]]))

(defn send-email [email log-content]
  (trace/trace "Sending email to:" email)

  (let [log-file "C:\\Users\\stank\\OneDrive\\Documents\\User.txt"
        log-file-content (str log-content)]
    (trace/trace "Type of log-file-content:" (type log-file-content))
    (trace/trace "Type of log-file:" (type log-file))
    (trace/trace "Log file content:" log-file-content)
    (trace/trace "Log file content:" log-file-content)

    (let [mail-settings {:host (System/getenv "SMTP_HOST")
                         :port 587
                         :user (System/getenv "SMTP_USER")
                         :pass (System/getenv "SMTP_APP_PASSWORD")
                         :tls true}
          email-data {:from (System/getenv "SMTP_USER")
                      :to email
                      :subject "Selected Logs"
                      :body "Please find the selected logs attached."
                      :attachments [{:content "Test log content directly inserted into email"
                                     :type "text/plain"
                                     :name "log.txt"}]}]
      (try
        (postal/send-message mail-settings email-data)
        (log/info "Email sent successfully.")
        (catch Exception e
          (log/error e "Failed to send email"))))))

(defn send-email-handler [request]
  (let [email (get-in request [:multipart-params "email"])
        log-content (get-in request [:multipart-params "logContent"])
        subject "Log File"
        body "Hi, please check out these logs"
        tmp-file (java.io.File/createTempFile "log-content-" ".txt")]
    (if (and email log-content)
      (do
        (spit tmp-file log-content)
        (send-email-with-attachment email subject body (.getAbsolutePath tmp-file))
        (response/response "Email sent successfully!"))
      (response/response "Missing email or log content"))))

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

(def clients (atom {}))
(defn start-trace-handler [request]
  (let [body (slurp (:body request))
        {:keys [path]} (json/parse-string body true)]
    (if (and path (.exists (io/file path)))
      (do
        (watch-file path clients)
        (response/response (json/generate-string
                            {:status "success"
                             :message (str "Started watching: " path)})))
      (response/response (json/generate-string
                          {:status "failed"
                           :message "Invalid or non-existing file path"})))))
(defn websocket-handler [req]
  (with-channel req channel
    (if-not channel
      (println "Channel is nil — something's wrong (this shouldn't happen)")
      (do
        (println "WebSocket connection established.")
        (swap! clients assoc channel {:email nil :file nil})
        (on-receive channel
                    (fn [data]
                      (let [{:keys [type email]} (json/parse-string data true)]
                        (cond
                          (= type "register")
                          (do
                            (println "Registered email for channel:" email)
                            (swap! clients assoc channel {:email email})
                            (send! channel (json/generate-string {:status "ok" :message "Email registered"})))

                          :else
                          (send! channel (json/generate-string {:status "error" :message "Unknown message type"}))))))
        (on-close channel
                  (fn [status]
                    (println "WebSocket connection closed, status:" status)
                    (swap! clients dissoc channel)))))))


  (defroutes private-routes
    (wrap-current-user-id
     (context "" {:keys [user-id]}
       (GET "/upload" []
         (if user-id
           (upload (get-user user-id))
           (response/redirect "/login")))
       (GET "/home" []
         (if user-id
           (home/home (get-user user-id))
           (response/redirect "/login")))
       (GET "/trace" []
         (if user-id
           (trace (get-user user-id))
           (response/redirect "/login")))
       (GET "/emails" [] (fetch-friends (get-user user-id)))
       (POST "/add-friend" request (add-friend request)))
     ))

  (defroutes app-routes
    (POST "/upload" request
      (upload-file-handler request))
    (POST "/trace" request
      (start-trace-handler request))
    (POST "/send-email" request (send-email-handler request))
    (GET "/ws" request
      (websocket-handler request))
    auth-routes
    friends-routes
    private-routes)

  (def app
    (-> (routes app-routes)
        (wrap-cors :access-control-allow-origin [#"http://localhost:3000/"]
                   :access-control-allow-headers ["Content-Type"]
                   :access-control-allow-methods [:get :post :options])
        (wrap-multipart-params)
        (wrap-params)
        (wrap-session)
        (wrap-resource "public")))
