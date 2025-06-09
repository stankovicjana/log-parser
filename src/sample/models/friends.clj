(ns sample.models.friends
  (:require
   [cheshire.core :as json]
   [clojure.java.jdbc :as jdbc]
   [clojure.java.jdbc :as sql]
   [clojure.tools.trace :as trace]
   [ring.util.response :as response]
   [sample.db :refer :all]))

(defn fetch-friends [user-id]
  (trace/trace "requst:" user-id)
  (let [user-id-value (:id user-id) 
        friends (jdbc/query db
                            ["SELECT f.friend_email FROM friends f WHERE f.user_id = ?" user-id-value])]
    (trace/trace "friends:" friends) 
    (response/response
     (json/generate-string
       {:emails (map :friend_email friends)}))))

(defn add-friend [request]
   (let [body (json/parse-string (slurp (:body request)) true)
         email (:email body)
         user-id (:user_id body)]
     (jdbc/insert! db :friends {:user_id user-id :friend_email email})
     (response/response (json/generate-string {:success true}))) 
    )