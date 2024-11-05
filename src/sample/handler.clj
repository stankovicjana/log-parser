(ns sample.handler
   (:require [compojure.core :refer [defroutes GET POST]]
             [ring.middleware.multipart-params :refer [wrap-multipart-params]]
             [ring.util.response :refer [response]]
             [sample.parser :refer [logs process-logfile]]
             [sample.views.home :refer [home-page]]))
  
  (defn upload-file-handler [request]
    (let [file (get-in request [:params "file"])]
      (when file
        (let [filename (:tempfile file)]
          (let [logs (process-logfile filename)] 
            (response (str "We found " (count logs) " errors")))))))
  
  (defroutes app-routes
    (GET "/" [] (home-page))
    (POST "/upload" request (upload-file-handler request)))
  
  (def app
    (-> app-routes
        (wrap-multipart-params)))
