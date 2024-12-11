(ns test
  (:require [postal.core :as postal]))

(defn send-test-email [to-email subject body]
  (let [smtp-user "stankovicjana@yahoo.com"
        smtp-pass "xgywgekiuobiokvh" 
        smtp-server "smtp.mail.yahoo.com"
        smtp-port 465]  
    (postal/send-message
     {:host smtp-server
      :port smtp-port
      :user smtp-user
      :pass smtp-pass
      :ssl true}
     {:from smtp-user
      :to to-email
      :subject subject
      :body body})))

;; Testiranje slanja e-maila
(send-test-email "stankovicjana000@gmail.com" "Test Subject" "Test Body")