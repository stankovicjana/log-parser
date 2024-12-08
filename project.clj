(defproject log-processor "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [compojure "1.7.1"]
                 [ring/ring-defaults "0.3.4"]
                 [midje "1.10.10"]
                 [org.clojure/tools.trace "0.7.11"]
                 [org.postgresql/postgresql "42.5.4"] 
                 [org.clojure/java.jdbc "0.7.12"] 
                 [migratus "1.3.5"] 
                 [ring/ring-mock "0.4.0"]
                 [com.draines/postal "2.0.5"]]

  :plugins [[lein-ring "0.12.5"]
            [migratus-lein "0.5.0"]]

  :ring {:handler sample.handler/app
         :init sample.handler/init}

  :migratus {:store :database
             :migration-dir "migrations"
             :db (or (System/getenv "DATABASE_PARSER") "postgresql://localhost:5432/parser")} 

  :profiles
  {:dev {:dependencies [[ring/ring-mock "0.4.0"]]}
   :test {:prep-tasks [["migratus" "migrate"]]}}) 
