(ns sample.routes.friends
  (:require [compojure.core :refer :all]
            [ring.util.response :refer [redirect]]
            [clojure.java.jdbc :as jdbc]
            [sample.db :refer [db]]))

(defroutes friends-routes

  (POST "/friends/add" [friend_email :as req]
    (let [user-id (get-in req [:session :user-id])]
      (jdbc/insert! db :friends {:user_id user-id :friend_email friend_email})
      (redirect "/home")))

  (POST "/friends/update" [old_email new_email :as req]
    (let [user-id (get-in req [:session :user-id])]
      (jdbc/update! db :friends
                    {:friend_email new_email}
                    ["user_id = ? AND friend_email = ?" user-id old_email])
      (redirect "/home")))

  (POST "/friends/delete" [email :as req]
    (let [user-id (get-in req [:session :user-id])]
      (jdbc/delete! db :friends
                    ["user_id = ? AND friend_email = ?" user-id email])
      (redirect "/home"))))
