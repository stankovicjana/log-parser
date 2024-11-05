(ns sample.parser
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn logfile-lines [filename]
  (string/split-lines (slurp filename)))

(defn contains-error? [line]
  (re-find #"(?i)error" line))

(defn parse-lines [lines]
  (filter contains-error? lines))

(defn process-logfile [filename]
  (parse-lines (logfile-lines filename)))

(defn logs [filenames]
  (mapcat process-logfile filenames))
