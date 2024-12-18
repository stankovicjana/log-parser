(ns sample.models.user
  (:require [clojure.java.jdbc :as sql]
            [sample.db :refer :all]))

(defn get-user-by-email [email]
  (sql/query db
             ["SELECT * FROM users WHERE email = ?", email]
             {:result-set-fn first}))
(defn get-all-users []
  (sql/query db
             ["SELECT * FROM users"]))

(defn get-user-by-id [id]
  (sql/query db
             ["SELECT * FROM users WHERE id = ?", id]
             {:result-set-fn first}))

(defn create-user [user]
  (sql/insert! db :users user))
