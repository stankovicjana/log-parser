(ns sample.views.trace
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]))
(defn trace-page [user]
  (let [user-id (:id user)]
    [:div.content
     [:p  
      "This tool helps you trace errors in your important files."
      [:br]
      "Firstly, select the email you want the warning message to be sent to in case of an error."
      [:br]
      "Choose from the list or enter new email."
      [:br]
      "Secondly, choose wanted log from file system."
      [:br]
      "If error occurs, message will be sent to your email."]
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
        [:h4 "Enter full path to an existing log file on the server"]
        [:input {:type "text" :id "file-path" :placeholder "E.g. C:/Users/Jana/Desktop/log.txt"}]
        [:button {:type "button" :id "watch-path-btn"} "Start Watching This Path"]]]
      [:div {:id "response-message" :style {:margin-top "20px"}}]]
     [:script
      (str "var userId = " user-id ";") "const socket = new WebSocket('http://localhost:3000/ws');
            socket.onopen = () => console.log('WebSocket connected');
      socket.onmessage = (event) => {
          const data = JSON.parse(event.data);

          if (data.status === 'ok' && data.message === 'Email registered') {
             console.log('Email registered successfully.');
              return;
          }
          if (data.alert) {
              alert('Error detected: ' + data.message);
          }
          document.getElementById('response-message').textContent = data.content;
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
      
         document.getElementById('watch-path-btn').addEventListener('click', function () {
        const path = document.getElementById('file-path').value;
        const email = document.getElementById('custom-textbox').value;

       if (!email) {
            alert('Please enter your email address first.');
            return;
        }

        socket.send(JSON.stringify({
            type: 'register',
            email: email
        }));

        if (path) {
            fetch('/trace', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ path: path })
            })
            .then(response => response.json())
            .then(data => {
                alert(data.message || 'Started watching the file.');
            })
            .catch(error => {
                console.error('Error starting watch:', error);
                alert('Error starting file watch.');
            });
        } else {
            alert('Please enter a valid file path.');
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
