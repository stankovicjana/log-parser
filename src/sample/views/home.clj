(ns sample.views.home
 (:require [hiccup.page :refer [html5 include-css]]))
 
 (defn home-page []
   (html5
    [:head
     [:title "Upload Logs"]
     (include-css "/css/style.css")]
    [:body
     [:h1 "Upload Logs"]
     [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
      [:input {:type "file" :name "file"}]
      [:input {:type "submit" :value "Upload"}]]]))