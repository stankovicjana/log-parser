(ns sample.views.upload
  (:require [sample.views.layout :refer [common]])) 

(defn upload-page []
  (common
   [:div.content
    [:h2 "Upload your file here:"]
    [:form {:id "upload-form" :action "/upload" :method "post" :enctype "multipart/form-data"}
     [:div
      [:input {:id "file-upload" :type "file" :name "file" :style "display:none;"}]
      [:label {:for "file-upload" :class "custom-file-upload"} "Choose File"]
      [:span {:id "file-name"} "No file chosen"]  
      [:button {:type "submit"} "Upload file"]]]
    [:div {:id "result"}]
    [:h3 "Log Data"]
    [:table {:border "1" :cellspacing "0" :cellpadding "5"}
     [:thead
      [:tr
       [:th "Timestamp"]
       [:th "Owner"]
       [:th "Message"]]]
     [:tbody {:id "log-table-body"}]]]
  ))