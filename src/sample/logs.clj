(ns sample.logs
  (:require [clojure.string :as s])
  (:import [java.nio.file Paths StandardWatchEventKinds]
           [java.net URI]))
(defn watch-file
  []
  (let [file-path (URI/create "file:///C:/new/test.txt")
        path (Paths/get file-path)
        parent-path (.getParent path)
        watch-service (.newWatchService (.getFileSystem path))]
    (.register parent-path watch-service (into-array [StandardWatchEventKinds/ENTRY_MODIFY]))
    (println "Monitoring changes to file:" file-path)
    (future
      (loop []
        (let [key (.take watch-service)]
          (doseq [event (.pollEvents key)]
            (when (= (.kind event) StandardWatchEventKinds/ENTRY_MODIFY)
              (let [updated-file (str (.resolve parent-path (.context event)))
                    content (slurp updated-file)]
                (println "File updated:" updated-file)
                (println "File content read:" content)
                (when (some #(s/includes? (s/lower-case %) "error") (s/split content #"\n"))
                  (println "Error detected in file! Updated content:")
                  (println content)))))
          (.reset key)
          (recur))))))



(watch-file)