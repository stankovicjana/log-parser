(ns sample.routes.auth
  (:require
   [compojure.core :refer :all]
   [hiccup.form :refer :all]
   [postal.core :refer [send-message]]
   [ring.util.response :as response]
   [sample.crypt :as crypt]
   [sample.helpers :refer :all]
   [sample.models.user :as db]
   [sample.views.layout :as layout]
   [sample.views.auth :as view]
   [struct.core :as st]))

(defn login-page [& [email errors]]
  (layout/common
   (view/login-page email errors)))

(defn registration-page [& [email errors]]
  (layout/common
   (view/registration-page email errors)))

(defn user-to-session [user]
  {:user-id (:id user)
   :user-name (:name user)
   :user-email (:email user)})

(defn handle-login [email password]
  (let [user (db/get-user-by-email email)]
    (if (and user (crypt/verify password (:encrypted_password user)))
      (assoc (response/redirect "/upload") :session (user-to-session user))
      (login-page email {:email "Email or password is invalid"}))))

(defn handle-logout []
  (assoc (response/redirect "/") :session nil))

(def auth-register-scheme
  {:name [st/required st/string]
   :email [st/required st/email]
   :password [st/required [st/min-count 6]]
   :password-confirmation [st/required [st/identical-to :password]]})

(defn validate-user [name email password password-confirmation]
  (st/validate {:name name
                :email email
                :password password
                :password-confirmation password-confirmation} auth-register-scheme))

(defn handle-registration [name email password password-confirmation]
  (let [errors (first (validate-user name email password password-confirmation))]
    (if errors
      (registration-page name email errors)
      (if (db/get-user-by-email email)
        (registration-page name email {:email "User with the same email already exists"})
        (do
          (db/create-user {:name name :email email :encrypted_password (crypt/encrypt password)})
          (let [user (db/get-user-by-email email)]
            (when (System/getenv "SMTP_FROM")
              (println (send-message {:user (System/getenv "SMTP_USER")
                                      :pass (System/getenv "SMTP_APP_PASSWORD")
                                      :host (System/getenv "SMTP_HOST")
                                      :port 587
                                      :tls true}
                                     {:from (System/getenv "SMTP_FROM")
                                      :to (:email user)
                                      :subject "Account Registration"
                                      :body "You successfully created an account"})))
            (assoc (response/redirect "/") :session (user-to-session user))))))))

(defroutes auth-routes
  (GET "/login" [] (login-page))
  (GET "/logout" [] (handle-logout))
  (GET "/register" [] (registration-page))
  (POST "/login" [email password] (handle-login email password))
  (POST "/register" [name email password password-confirmation]
    (handle-registration name email password password-confirmation))
  (GET "/users" []
    (response/response (db/get-all-users)))) 
