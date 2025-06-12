(ns sample.models.emails
 (:require [clojure.java.jdbc :as sql]
            [sample.db :refer :all]))
 
 (defn save-sent-email! [user-id recipient subject body]
   (sql/insert! db
                :sent_emails
                {:user_id user-id
                 :recipient_email recipient
                 :subject subject
                 :body body}))