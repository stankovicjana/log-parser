(defproject log-processor "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [compojure "1.7.1"]
                 [ring/ring-defaults "0.3.4"]]
  :plugins [[lein-ring "0.12.5"]]
            :ring {:handler sample.handler/app
                   :init sample.handler/init}
            :profiles
            {:dev {:dependencies [[ring/ring-mock "0.4.0"]]}
             :test {:prep-tasks [["migratus", "migrate"]]}})
