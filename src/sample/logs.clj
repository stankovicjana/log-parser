(ns sample.logs
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [org.httpkit.server :refer [send!]]))

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

                (doseq [channel (keys @clients)]
                  (try
                    (send! channel (json/generate-string msg))
                    (catch Exception e
                      (println "Failed to send to client:" (.getMessage e)))))))

            (Thread/sleep 5000)
            (recur)))))))