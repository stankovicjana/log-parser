(ns sample.helpers
  (:require [compojure.core :refer :all]
            [sample.models.user :as user-db]
            [ring.util.codec :refer [url-encode]]
            [hiccup.form :refer :all]
            [hiccup.element :refer :all]
            [hiccup.form :refer [label]]
            [clojure.string :as str]
            [postal.core :refer [send-message]]
            [postal.message :as msg])
  (:import
   [java.net Socket]
   [java.nio.file Files Paths]))

(defn get-user [id]
  (if id
    (user-db/get-user-by-id id)))

(defn error-item [error]
  [:div.text-danger error])

(defn input-control [type id name & [value required error]]
  [:div.form-group
   [:label {:for id} name]
   (when error (error-item error))
   [:input {:type type
            :id id
            :name id
            :value value
            :required required
            :class "form-control"}]])

(defn add-extra [^javax.mail.Message jmsg email-data]
  "Add headers, recipients, and attachments to the message."
  (let [{:keys [to from cc bcc subject attachments]} email-data]
    (when from
      (.setFrom jmsg (javax.mail.internet.InternetAddress. from)))

    (doseq [recipient-type [:to :cc :bcc]]
      (when-let [recipients (get email-data recipient-type)]
        (doseq [recipient recipients]
          (.addRecipient jmsg
                         (case recipient-type
                           :to javax.mail.Message$RecipientType/TO
                           :cc javax.mail.Message$RecipientType/CC
                           :bcc javax.mail.Message$RecipientType/BCC)
                         (javax.mail.internet.InternetAddress. recipient)))))
    (when subject
      (.setSubject jmsg subject))

    (when attachments
      (let [multipart (javax.mail.internet.MimeMultipart.)]
        (let [body-part (javax.mail.internet.MimeBodyPart.)]
          (.setText body-part (:body email-data))
          (.addBodyPart multipart body-part))

        (doseq [attachment attachments]
          (let [attachment-part (javax.mail.internet.MimeBodyPart.)
                data-source (javax.mail.util.ByteArrayDataSource.
                             (:content attachment)
                             (:content-type attachment))]
            (.setDataHandler attachment-part
                             (javax.activation.DataHandler. data-source))
            (.setFileName attachment-part (:file-name attachment))
            (.addBodyPart multipart attachment-part)))

        (.setContent jmsg multipart))))
  jmsg)

(defn send-email-with-attachment
  "Send an email with an attachment."
  [to-email subject body file-path]
  (let [file-bytes (java.nio.file.Files/readAllBytes (java.nio.file.Paths/get file-path (into-array String [])))
        file-name (last (clojure.string/split file-path #"[\\/]+"))
        attachment {:content-type "application/octet-stream"
                    :content file-bytes
                    :file-name file-name
                    :type :attachment}
        email-data {:from (System/getenv "SMTP_USER")
                    :to [to-email]
                    :subject subject
                    :body body
                    :attachments [attachment]}
        mail-settings
        {:host (System/getenv "SMTP_HOST")
         :port 587
         :user (System/getenv "SMTP_USER")
         :pass (System/getenv "SMTP_APP_PASSWORD")
         :tls true}]
    (let [props (doto (java.util.Properties.)
                  (.put "mail.smtp.starttls.enable" (str (:tls mail-settings)))
                  (.put "mail.smtp.host" (:host mail-settings))
                  (.put "mail.smtp.port" (str (:port mail-settings)))
                  (.put "mail.smtp.auth" "true"))
          session (javax.mail.Session/getInstance props
                                                  (proxy [javax.mail.Authenticator] []
                                                    (getPasswordAuthentication []
                                                      (javax.mail.PasswordAuthentication. (:user mail-settings) (:pass mail-settings)))))
          jmsg (javax.mail.internet.MimeMessage. session)]

      (try
        (println "SMTP Properties:" props)
        (println "Email data:" email-data)
        (add-extra jmsg email-data)
        (javax.mail.Transport/send jmsg)
        (println "Email sent successfully with attachment.")
        (catch Exception e
          (println "Error sending email:" (.getMessage e))
          (println "Stack trace:")
          (.printStackTrace e))))))
