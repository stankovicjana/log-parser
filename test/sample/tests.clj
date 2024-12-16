(ns sample.tests
  (:require [midje.sweet :refer :all]
            [postal.core :as postal]))

(fact "test"
      (+ 1 2) => 3)


(def mail-settings {:host (System/getenv "SMTP_HOST")
                    :port 587
                    :user (System/getenv "SMTP_USER")
                    :pass (System/getenv "SMTP_APP_PASSWORD")
                    :tls true})

(def email-data {:from (System/getenv "SMTP_USER")
                 :to "stankovicjana000@gmail.com"
                 :subject "Test Email"
                 :body "This is a test email."})

(postal/send-message mail-settings email-data)
