(ns sample.parser
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.edn :as edn]))

(defn logfile-lines [filename]
  (string/split-lines (slurp filename)))


(defn parse-line [line]
  (let [regex #"(.*)\s+\[(.*)\]\s+(.*)"  
        match (re-matches regex line)]
    (if match
      {:timestamp (string/trim (nth match 1))  
       :owner (string/trim (nth match 2))     
       :message (string/trim (nth match 3))}  
      nil)))  

(defn parse-lines [lines]
  (->> lines
       (map parse-line)  
       (filter some?)))  

(defn process-logfile [filename]
  (parse-lines (logfile-lines filename)))

(defn logs [filenames]
  (mapcat process-logfile filenames))
