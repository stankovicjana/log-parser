(ns sample.helpers
  (:require [compojure.core :refer :all]
             [sample.models.user :as user-db]
             [ring.util.codec :refer [url-encode]]
             [hiccup.form :refer :all]
             [hiccup.element :refer :all]
             [hiccup.form :refer [label]]))


(defn error-item [error]
  [:div.text-danger error])
(defn input-control [type id name & [value required error]]
  [:div.form-group
   (list
    (label id name)
    (if error (error-item error))
    [:input (merge {:type type :id id :name name :class "form-control"}
                   (when value {:value value})
                   (when required {:required "required"}))])])

