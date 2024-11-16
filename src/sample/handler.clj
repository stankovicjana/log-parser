(ns sample.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.util.response :refer [response]]
            [ring.middleware.resource :refer [wrap-resource]]
            [sample.parser :refer [logs process-logfile]]
            [sample.views.home :refer [home-page]]
            [sample.views.report :refer [report-page]]
            [sample.views.upload :refer [upload-page]] 
            [cheshire.core :as json]))
  
 (defn upload-file-handler [request]
   (let [file (get-in request [:params "file"])]
     (if file
       (let [filename (:tempfile file)
             logs (process-logfile filename)]  
         (response (json/generate-string logs))) 
       (response (json/generate-string [])))))  

  (defroutes app-routes
    (GET "/" [] (home-page)) 
    (GET "/report" [] (report-page)) 
    (GET "/upload" [] (upload-page))
    (POST "/uploadFile" request (upload-file-handler request)))

  (def app
    (-> app-routes
        (wrap-resource "public" )      
        (wrap-multipart-params)))