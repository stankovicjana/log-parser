(ns sample.views.upload) 

(defn upload-page [user]
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
      [:th "Select"]
      [:th "Timestamp"]
      [:th "Owner"]
      [:th "Message"]]]
    [:tbody {:id "log-table-body"}]]
   [:div {:class "sendLogs" :style "display:none;"}
   [:h2 "Share your insights"]
   [:p "Enter email to share selected log info."]
   [:input {:type "text" :id "custom-textbox" :placeholder "Enter email"}]
   [:button {:type "button" :id "custom-button"} "Send"]]
   ])
