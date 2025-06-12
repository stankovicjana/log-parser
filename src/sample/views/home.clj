(ns sample.views.home
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer [form-to submit-button]]
            [sample.helpers :refer [input-control]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))
(defn home-page [user]
  [:div.content
   [:div
     [:h1 "Welcome to the Log Analysis Tool"]
    [:p "This web application helps you monitor and analyze logs in a simple and efficient way."]
    [:br]
    [:ul
     [:li "You can monitor specific log files for error occurrences."]
     [:li "You will be notified via email when an error is detected."]
     [:li "You can parse logs to improve readability and assist in debugging."]
     [:li "Parsed logs can be emailed to others for further analysis."]
     [:li "Registration and login are required to use the application."]]
    [:br]
    [:p "Use the navigation bar above to explore available features."]
    ]
   [:div.profile-friends-wrapper
    [:div.profile-card
   [:h2 "Your Profile"] 
     [:div.profile-info
      [:div.row
       [:span.label "Name:"]
       [:span.value (:name user)]]
      [:div.row
       [:span.label "Email:"]
       [:span.value (:email user)]]]]
    [:dik.friends-card
       [:h2 "Your Friends"]
       [:table
        [:thead
         [:tr
          [:th "Email"]
          [:th "Actions"]]]
        [:tbody
         (for [[idx email] (map-indexed vector (:friends user))]
           [:tr {:key idx}
            [:td
             [:form {:method "post" :action "/friends/update" :style "margin:0;"}
              (anti-forgery-field)
              [:input {:type "hidden" :name "old_email" :value email}]
              [:input.form-control {:type "text" :name "new_email" :value email}]]]
        
            [:td
             [:div {:style "display: flex; gap: 6px;"}
              [:form {:method "post" :action "/friends/update" :style "margin: 0;"}
               (anti-forgery-field)
               [:input {:type "hidden" :name "old_email" :value email}]
               [:input {:type "hidden" :name "new_email" :value email}]
               [:button.save-btn {:type "submit"} "Save"]]
            
              [:form {:method "post" :action "/friends/delete" :style "margin: 0;"}
               (anti-forgery-field)
               [:input {:type "hidden" :name "email" :value email}]
               [:button.delete-btn {:type "submit"} "Delete"]]]]])]

        [:tfoot
         [:tr
          [:form {:method "post" :action "/friends/add"}
           (anti-forgery-field)
           [:td [:input.form-control {:type "text" :name "friend_email" :placeholder "Enter new friend's email"}]]
           [:td [:button.add-btn {:type "submit"} "Add"]]]]]]]
           
   ]
           
           
  ])