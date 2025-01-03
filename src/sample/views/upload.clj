(ns sample.views.upload) 
(defn upload-page [user]
  (let [user-id (:id user)]
    [:div.content
     [:p "This tool helps you parse complicated and long logs and analyse them.
          When log is uploaded, you instantly see lines that have error, considering they are red.
          There is option to mark line yellow by clicking on it.
          You can also select part of the log and send it to the person of interest for further analyzation."]
     [:h2 "Upload your file here:"]
     [:form {:id "upload-form" :action "/upload" :method "post" :enctype "multipart/form-data"}
      [:div {:id "upload-div"}
       [:div
        [:input {:id "file-upload" :type "file" :name "file" :style "display:none;"}]
        [:label {:for "file-upload" :class "custom-file-upload"} "Choose File"]
        [:span {:id "file-name"} "No file chosen"]]
       [:div
        [:button {:type "submit"} "Upload file"]]]]
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
      [:p "Enter or select email to share selected log info."]
      [:div {:id "trace-div"}
       [:div {:id "upload-div"}
        [:input {:list "email-list" :id "custom-textbox" :placeholder "Enter or select email"}]
        [:datalist {:id "email-list"}]]
       [:div {:id "submit-div"}
        [:button {:type "button" :id "add-button"} "Add friend"]
        [:button {:type "button" :id "custom-button"} "Send"]]
       ]
      ]
     [:script
      (str "var userId = " user-id ";")
      "fetch('/emails')  
         .then(response => response.json())
         .then(data => {
            const emailList = document.getElementById('email-list');
            data.emails.forEach(email => {
               const option = document.createElement('option');
               option.value = email;
               option.textContent = email;  
               emailList.appendChild(option);
            });
         })
         .catch(error => console.error('Error fetching email list:', error));"]]))
