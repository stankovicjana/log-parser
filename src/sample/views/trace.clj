(ns sample.views.trace
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]))

(defn trace-page [user]
  [:div.content
   [:h2 "Choose the log you want to trace:"]
   [:form {:id "trace-form" :action "/trace" :method "post" :enctype "multipart/form-data"}
    [:div
     [:input {:id "file-trace" :type "file" :name "file" :style "display:none;"}]
     [:label {:for "file-trace" :class "custom-file-upload"} "Choose File"]
     [:span {:id "file-name"} "No file chosen"]
     [:button {:type "submit" :id "trace-btn"} "Trace file"]]
    [:div {:id "response-message" :style {:margin-top "20px"}}]]
  [:script
     "document.getElementById('file-trace').addEventListener('change', function(event) {
          document.getElementById('file-name').textContent = event.target.files[0].name;
      });
      document.getElementById('trace-form').onsubmit = function(event) {
          event.preventDefault();  
          var formData = new FormData(document.getElementById('trace-form'));
          var xhr = new XMLHttpRequest();
          xhr.open('POST', '/trace', true);  
          xhr.onload = function () {
              if (xhr.status === 200) {
                  try {
                      var data = JSON.parse(xhr.responseText);  
                      var responseMessageDiv = document.getElementById('response-message');
                      
                      if (data.status === 'success') {
                          responseMessageDiv.innerHTML = '<p style=\"color: green;\">' + data.message + '</p>';
                      } else {
                          responseMessageDiv.innerHTML = '<p style=\"color: red;\">' + data.message + '</p>';
                      }
                  } catch (e) {
                      alert('Error parsing response: ' + e.message);
                  }
              } else {
                  alert('Error: ' + xhr.status);
              }
          };
          xhr.onerror = function () {
              alert('Request failed. Please try again later.');
          };
          xhr.send(formData);  
      };
      "]])  
