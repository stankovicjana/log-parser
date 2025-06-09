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
     [:form {:id "trace-form"}
      [:div {:id "trace-div"}
       [:div
        [:input {:id "file-trace" :type "file" :name "file" :style "display:none;"}]
        [:label {:for "file-trace" :class "custom-file-upload"} "Choose File"]
        [:span {:id "file-name"} "No file chosen"]]
       [:div {:id "submit-div"}
        [:button {:type "button" :id "trace-btn"} "Start Tracing"]
        [:button {:type "button" :id "cancel-btn"} "Cancel tracing"]]]
      [:div {:id "response-message" :style {:margin-top "20px"}}]]
     [:script
      (str "var userId = " user-id ";")
      "const socket = new WebSocket('http://localhost:3000/ws');

      socket.onopen = () => console.log('WebSocket connected');

      socket.onmessage = (event) => {
          console.log('Message from server:', event.data);
      };

      document.getElementById('add-button').addEventListener('click', function() {
          const email = document.getElementById('custom-textbox').value;  
          if (email) {
              fetch('/add-friend', {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({ email, user_id: userId })
              })
              .then(response => response.json())
              .then(data => alert('Friend added successfully!'))
              .catch(error => alert('Failed to add friend.'));
          } else {
              alert('Please enter an email to add a friend.');
          }
      });

      document.getElementById('file-trace').addEventListener('change', function(event) {
          const file = event.target.files[0];
          document.getElementById('file-name').textContent = file ? file.name : 'No file chosen';

          if (file) {
              const reader = new FileReader();
              let lastContent = '';

              const monitorFile = () => {
                  reader.onload = () => {
                      const currentContent = reader.result;
                      if (currentContent !== lastContent) {
                          lastContent = currentContent;
                          socket.send(JSON.stringify({
                              fileName: file.name,
                              content: currentContent
                          }));
                          console.log('File updated and sent to server:', file.name);
                      }
                  };
                  reader.readAsText(file);
              };

              setInterval(monitorFile, 5000);
          }
      });

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
