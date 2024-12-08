(ns sample.views.auth)

(defn login-page [& [email errors]]
  [:div.login-form
   [:h1 "Login with existing account"]
   ])


(defn registration-page [& [name email errors]]
  [:div.registration-form
   [:h1 "Create an account"]
   ])