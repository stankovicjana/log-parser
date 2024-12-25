(ns sample.models.friends
  (:require
   [cheshire.core :as json]
   [clojure.java.jdbc :as jdbc]
   [clojure.tools.trace :as trace]
   [ring.util.response :as response]
   [sample.db :refer :all]))

(defn fetch-friends [user-id]
  (trace/trace "doslo:" user-id)
  (let [user-id-value (:id user-id)  ;; Ekstrahujte :id iz user-id mape
        friends (jdbc/query db
                            ["SELECT f.friend_email FROM friends f WHERE f.user_id = ?" user-id-value])]
    
    (trace/trace "prijatelji:" friends) 
    (response/response
     (json/generate-string
       {:emails (map :friend_email friends)}))))