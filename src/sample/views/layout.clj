(ns sample.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [link-to]]))
(defn guest-menu []
  (list
   [:ul {:class "nav navbar-nav navbar-right"}
    [:li
     [:a {:class "login" :href "/login"} "Login"]]
    [:li
     [:a {:class "register" :href "/register"} "Register"]]]))
(defn user-menu [user]
  [:ul {:class "nav navbar-nav navbar-right"}
   [:li
    [:a {:class "trace" :href "/trace"} "Trace files"]]
   [:li
    [:a {:class "upload" :href "/upload"} "Upload and parse file"]]
   [:li
    [:a {:class "logout" :href "/logout"} "Logout"]]])
(defn base [& content]
  (html5
   [:head
    [:meta {:http-equiv "content-type" :content "text/html; charset=UTF-8"}]
    [:meta {:name "description" :content "Parsing files and tracing application"}]
    [:meta {:name "keywords" :content "images pictures"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title "Parsing files and tracing application"]
    (include-css "/css/style.css")
    ]
   [:body content]
   )) 
(defn common [& [content user]]
  (base
   [:header.navbar.navbar-default.navbar-static-top
    [:nav
     [:div.navbar-header
      [:a.navbar-brand {:href "/" :class "navbar-link"} ""]]
     (if user
       (do
         (user-menu user)) 
       (guest-menu))]]
   [:div {:class ""} content]
   (include-js "/js/upload.js")
   (include-js "/js/trace.js")))
