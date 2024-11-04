(ns sample.handler
  (:require [compojure.core :refer [defroutes GET]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))
 
 (defroutes app-routes
   (GET "/" [] "<h1>Welcome</h1>"))
 
 (def app
   (wrap-defaults app-routes site-defaults))
 
 (defn init []
   (println "App is starting..."))
                   