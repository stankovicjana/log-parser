(ns sample.logs
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [org.httpkit.server :refer [send!]]
   [sample.helpers :refer [send-email-with-attachment]]))

(defn watch-file [file-path clients]
  (let [file (io/file file-path)
        last-content (atom nil)]
    (future
      (loop []
        (when (.exists file)
          (let [new-content (slurp file)]
            (when (not= @last-content new-content)
              (reset! last-content new-content)
              (println "File content changed...")

              (let [has-error? (re-find #"(?i)error|exception" new-content)
                    msg (if has-error?
                          {:alert true
                           :message "Error detected in log file!"
                           :file file-path
                           :content new-content}
                          {:alert false
                           :message "Log file updated"
                           :file file-path
                           :content new-content})]

                (doseq [[channel {:keys [email]}] @clients]
                  (try
                    (send! channel (json/generate-string msg))
                    (when (and has-error? email)
                      (let [tmp-file (java.io.File/createTempFile "log-error-" ".txt")]
                        (spit tmp-file new-content)
                        (send-email-with-attachment email
                                                    "Log Error Detected"
                                                    (str "Dear user,\n\nAn error was detected in file:\n" file-path)
                                                    (.getAbsolutePath tmp-file))))
                    (catch Exception e
                      (println "Failed to notify client or send email:" (.getMessage e)))))))

            (Thread/sleep 5000)
            (recur)))))))