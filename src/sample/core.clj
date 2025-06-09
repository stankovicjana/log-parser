(ns sample.core
  (:gen-class)
  (:require [org.httpkit.server :refer [run-server]]
            [sample.handler :refer [app]]))

(defn -main [& args]
  (println "Starting http-kit server on port 3000")
  (run-server app {:port 3000}))
