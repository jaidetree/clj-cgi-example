#!/usr/bin/env bb

(ns cgi.lib.index
  (:require
   [hiccup2.core :refer [html]]))

(println "Content-Type: text/html\n")

(println
 (str
  (html
   [:html
    [:head
     [:title "Libs"]
     [:link
      {:rel "stylesheet"
       :type "text/css"
       :href "/style.css"}]]
    [:body
     [:div.page
      [:h1 "Libraries with CGI-Bin Clojure"]
      [:p
       "It's recommended to put your libs in this directory. For development purposes leaving them exposed in an accessible directory should be fine but in production they are best place outside of it."]
      [:p
       "Don't forget to add your libary jars, add pods, and set their permissions appropriately. In this local docker development server you should be able to use Clojure, lein, and babashka to install the libs and pods you need from the shell. However in a production-grade shared host, that is usually not an option so you will need to uberjar or build those libraries locally then upload and use the static binary versions of pods and babashka as well."]]]])))
