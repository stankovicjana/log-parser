(ns sample.routes.home
  (:require
   [clojure.java.jdbc :as jdbc]
   [compojure.core :refer :all]
   [sample.db :refer [db]]
   [sample.helpers :refer :all]
   [sample.views.home :as view]
   [sample.views.layout :as layout]))

(defn home [user]
 (let [friends (map :friend_email
                     (jdbc/query db
                                 ["SELECT friend_email FROM friends WHERE user_id = ?" (:id user)]))
        enriched-user (assoc user :friends friends)]
    (layout/common (view/home-page enriched-user) enriched-user)))


  