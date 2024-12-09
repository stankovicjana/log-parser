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