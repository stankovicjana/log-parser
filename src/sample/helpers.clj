(ns sample.helpers
  (:require [compojure.core :refer :all]
             [sample.models.user :as user-db]
             [ring.util.codec :refer [url-encode]]
             [hiccup.form :refer :all]
             [hiccup.element :refer :all]
             [hiccup.form :refer [label]]))

(defn get-user [id]
  (if id
    (user-db/get-user-by-id id)))

(defn error-item [error]
  [:div.text-danger error])

(defn input-control [type id name & [value required error]]
  [:div.form-group
   (list
    (label id name)
    (if error (error-item error))
    (type {:class "form-control" :required required} id value))])
