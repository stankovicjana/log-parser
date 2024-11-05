(ns sample.views.home
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]))

(defn home-page []
  (html5
   [:head
    [:title "Upload Logs"]
    (include-css "/css/style.css")]
   [:body
    [:nav
     [:ul
      [:li (link-to "/" "Home")]
      [:li (link-to "/upload" "Upload file")]]]
    [:div {:class "container"}
     [:div {:class "sidebar"} 
      [:ul
       [:li (link-to "/" "Home")]
       [:li (link-to "/upload" "Upload file")]]]
     [:div {:class "content"} 
      [:h2 "Upload your file here:"]
      [:form {:action "/upload" :method "post" :enctype "multipart/form-data"}
       [:input {:type "file" :name "file"}]
       [:input {:type "submit" :value "Upload"}]]]]]))
