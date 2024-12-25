(ns sample.views.trace
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]))

(defn trace-page [user]
  (let [user-id (:id user)]
  [:div.content
   [:p "This tool helps you trace errors in your important files. 
        Firstly, select the email you want the warning message to be sent to in case of an error.
        Choose from the list or enter new email.
        Secondly, choose wanted log from file system.
        If error occurs, message will be sent to your email"]
   [:div
    [:h4 "Enter email to notify if error occurs"]
    [:div {:id "upload-div"}
     [:input {:list "email-list" :id "custom-textbox" :placeholder "Enter or select email"}]
     [:datalist {:id "email-list"}]
     [:div {:id "submit-div"}
      [:button {:type "button" :id "add-button"} "Add friend"]]]]
   [:h2 "Choose the log you want to trace:"]
   [:form {:id "trace-form" :action "/trace" :method "post" :enctype "multipart/form-data"}
    [:div {:id "trace-div"}
     [:div
     [:input {:id "file-trace" :type "file" :name "file" :style "display:none;"}]
     [:label {:for "file-trace" :class "custom-file-upload"} "Choose File"]
     [:span {:id "file-name"} "No file chosen"]]
     [:div {:id "submit-div"}
     [:button {:type "submit" :id "trace-btn"} "Trace file"]
     [:button {:type "submit" :id "cancel-btn"} "Cancel tracing"]]]
    [:div {:id "response-message" :style {:margin-top "20px"}}]]
  [:script
   (str "var userId = " user-id ";")
   "document.getElementById('add-button').addEventListener('click', function() {
          var email = document.getElementById('custom-textbox').value;  
          var userId = window.userId;
      
          if (email) {
              var newFriend = {
                  email: email,
                  user_id: userId
              };
      
              var xhr = new XMLHttpRequest();
              xhr.open('POST', '/add-friend', true); 
      
              xhr.setRequestHeader('Content-Type', 'application/json');  
      
              xhr.onload = function() {
                  if (xhr.status === 200) {
                      alert('Friend added successfully!');
                  } else {
                      alert('Failed to add friend.');
                  }
              };
              xhr.send(JSON.stringify(newFriend));
          } else {
              alert('Please enter an email to add a friend.');
          }
      });
  
      document.getElementById('file-trace').addEventListener('change', function(event) {
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
  
      fetch('/emails')  
      .then(response => response.json())
      .then(data => {
          const emailList = document.getElementById('email-list');
          data.emails.forEach(email => {
              const option = document.createElement('option');
              option.value = email;
              emailList.appendChild(option);
          });
      })
      .catch(error => console.error('Error fetching email list:', error));
    "]]))
