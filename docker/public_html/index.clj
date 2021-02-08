#!/usr/bin/env bb

(ns cgi.index
  (:require
   [hiccup2.core :refer [html]]))

;; Add jars
;; Add pods
;; Add sources to classpath

(println "Content-Type: text/html\n")

(println
 (str
  (html
   [:html
    [:head
     [:title "Hello"]
     [:link
      {:rel "stylesheet"
       :type "text/css"
       :href "/style.css"}]
     [:link {:rel "stylesheet"
             :href "https://fonts.googleapis.com/css2?family=Sriracha&display=swap"}]
     [:script
      {:src "https://kit.fontawesome.com/8c366f1e4e.js"
       :crossorigin "anonymous"}]]
    [:body
     [:div.page
      [:section
       [:h1
        [:i.fad.fa-hand-sparkles]
        " Hello"]
       [:p
        "Looks like you made it! This page is running a Clojure script through "
        [:a {:href "https://github.com/babashka/babashka"}
         "Babashka"]
        ". To start hacking on this site, look at "
        [:code "public_html/hello.clj"]
        " for a quick reference."]]
      [:section
       [:h2
        [:i.fad.fa-battery-full]
        " Batteries Included"]
       [:p
        "Unlike a production-grade shared host, this docker development container has bb, Clojure, the JVM, lein, and the build essentials already installed. This means you can directly install deps from the shell and use Babashka's automatic pod downloading capabilities."]]
      [:section
       [:h2
        [:i.fad.fa-info-circle]
        " More Help"]
       [:p
        "For more help take a look at an article that covers the setup for production-grade shared hosts:"
        [:br]
        [:a {:href "https://eccentric-j.com/blog/clojure-like-its-php.html"}
         "https://eccentric-j.com/blog/clojure-like-its-php.html "
         [:i.fad.fa-file-alt]]]
       [:p
        "Check out the repo that maintains this docker file for project updates and reocmmendations."
        [:br]
        [:a
         {:href "https://github.com/eccentric-j/clj-cgi-example"}
         "https://github.com/eccentric-j/clj-cgi-example "
         [:i.fab.fa-github]]]]
      [:section.hacking
       [:h3
        {:style {:border-bottom "none"
                 :padding-bottom 0
                 :color "#7cdbab"}}
        "Happy Hacking! "
        [:i.fad.fa-laptop-code]]]
      [:section.credit
       [:p
        {:style {:text-align "center"}}
        [:a
         {:href "https://eccentric-j.com"}
         [:img
          {:alt "Eccentric J"
           :src "/eccentric-j-logo.svg"
           :style {:width "10rem" :height "auto"
                   :margin-bottom "0.5rem"}}]]
        [:br]
        [:span
         {:style {:font-size "0.8rem"
                  :color "rgba(255, 255, 255, 0.6)"
                  :font-style "italic"}}
         "Powered by jaunty narcissism"]]]]]])))
