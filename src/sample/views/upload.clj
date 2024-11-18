(ns sample.views.upload
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]))

(defn upload-page []
  (html5
   [:head
    [:title "Upload Logs"]
    (include-css "/css/style.css")]
   [:body
    [:nav
     [:ul
      [:li (link-to "/" "My profile")]
      [:li (link-to "/" "Logout")]]]
    [:div {:class "container"}
     [:div {:class "sidebar"}
      [:ul
       [:li (link-to "/upload" "Upload file")]
       [:li (link-to "/report" "Generate report")]]]
     [:div {:class "content"}
      [:h2 "Upload your file here:"]
      [:form {:id "upload-form" :action "/uploadFile" :method "post" :enctype "multipart/form-data"}
       [:input {:type "file" :name "file" :id "file"}]
       [:button {:type "submit"} "Upload file"]]
      [:div {:id "result"}] [:h3 "Log Data"]
      [:table {:border "1" :cellspacing "0" :cellpadding "5"}
       [:thead
        [:tr
         [:th "Timestamp"]
         [:th "Owner"]
         [:th "Message"]]]
       [:tbody {:id "log-table-body"}]]]]

    [:script
     "document.getElementById('upload-form').onsubmit = function(event) {
        event.preventDefault();
        
        var formData = new FormData(document.getElementById('upload-form'));
        
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/uploadFile', true);
        
        xhr.onload = function () {
          if (xhr.status === 200) {
            try {
              var data = JSON.parse(xhr.responseText);  
              console.log('JSON Response:', data);
              var tableBody = document.getElementById('log-table-body');
              tableBody.innerHTML = '';  
              
              if (data.length > 0) {
                data.forEach(function(log) {
                  var row = document.createElement('tr');
                  row.innerHTML = '<td>' + log.timestamp + '</td><td>' + log.owner + '</td><td>' + log.message + '</td>';
                  tableBody.appendChild(row);
                });
              } else {
                var row = document.createElement('tr');
                row.innerHTML = '<td colspan=" 3 ">No data available</td>';
                tableBody.appendChild(row);
              }
            } catch (e) {
              alert('Error parsing response: ' + e.message);
            }
          } else {
            alert('An error occurred while uploading the file.');
          }
        };
        
        xhr.send(formData);
                                                  
        console.log('JSON Response2:', formData);

      };"]]))