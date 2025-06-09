(ns sample.parser
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.edn :as edn]))

;slurp čita sadržaj fajla kao jedan veliki string
(defn logfile-lines [filename]
  (string/split-lines (slurp filename)))

(defn parse-line [line]
  (let [regex #"(.*)\s+\[(.*)\]\s+(.*)"
        match (re-matches regex line)]
    (if match
      (let [owner (string/trim (nth match 2))]
        {:timestamp (string/trim (nth match 1))
         :owner owner
         :message (string/trim (nth match 3))
         :error (string/includes? (string/lower-case owner) "error")})
      nil)))

;->> je threading operator koji omogućava lakše čitanje sekvencijalnih transformacija.
;map parse-line primenjuje funkciju parse-line na svaku liniju.
;filter ? uklanja nil vrednosti, ostavljajući samo uspešno parsirane linije.
(defn parse-lines [lines]
  (->> lines
       (map parse-line)
       (filter some?)))

(defn process-logfile [filename]
  (parse-lines (logfile-lines filename)))

(defn logs [filenames]
  (mapcat process-logfile filenames))
