(ns sample.views.trace
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]))

(defn trace-page [user]
  [:div.content
   [:h2 "Choose the log you want to trace:"]
   [:select {:id "log-type"}
    [:option {:value "event-viewer"} "Event Viewer"]
    [:option {:value "system-log"} "System Log"]
    [:option {:value "application-log"} "Application Log"]
    [:option {:value "security-log"} "Security Log"]]
   [:div.trace
    [:div
     [:label "From: "]
     [:input {:type "date" :name "start-date" :id "start-date"}]]
    [:div
     [:label "To: "]
     [:input {:type "date" :name "end-date" :id "end-date"}]]]
   [:button {:type "button" :id "trace-btn"} "Retrive data"]
   [:div {:id "result"}]
   [:h3 "Log Data"]
   [:table {:border "1" :cellspacing "0" :cellpadding "5"}
    [:thead
     [:tr
      [:th "Level"]
      [:th "Date and Time"]
      [:th "Source"]
      [:th "Event ID"]
      [:th "Task Category"]]]
    [:tbody {:id "event-table-body"}]]]
  )

