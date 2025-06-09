(ns sample.logs
  (:require [clojure.string :as s])
  (:import (java.nio.file Paths StandardWatchEventKinds WatchService)
           (java.net URI)))

(defn watch-file [file-path]
  (let [file-path (URI/create (str "file:///" (s/replace file-path #"\\" "/")))
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
              (let [updated-file (str (.resolve parent-path (.context event)))]
                (println "File updated:" updated-file))))
          (.reset key)
          (recur))))))