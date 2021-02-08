(ns metal.guide
  (:require
   [gaka.core :refer [css]]
   [clojure.string :as s]
   [metal.style :refer [rems]]))

(def styles
  (css
   [:.guide
    {:background "#2D242D"
     :color "#fff"
     :padding "4rem 0"}
    ["h1, h2, h3"
     {:border-bottom "1px solid #7DDAAB"
      :text-align "center"
      :color "#7DDAAB"}]
    [:h1
     {:font-size (rems 50)}]
    [:h2
     {:font-size (rems 40)}]
    [:h3
     {:font-size (rems 30)}]
    [:h4
     {:font-size (rems 25)
      :color "#CC79B0"}]
    [:i
     {:font-size "0.8em"}]
    [:a
     {:color "#7DDAAB"
      :transition "all 250ms ease-in-out"
      :text-decoration "none"}]
    [:a:hover
     {:color "#CC79B0"
      :text-decoration "underline"}]
    ["h2, h3"
     {:font-family "Sriracha, cursive"}]
    [:.inner
     {:max-width "800px"
      :margin "0 auto"}]
    [:.p
     :font-size (rems 20)
     :line-height 1.8]
    [:.question
     [:h4 {:font-style :italic}]]
    [:.credits
     {:text-align "center"
      :margin 0
      :padding 0
      :font-size (rems 20)
      :line-height 1.8
      :list-style "none"}]]))

(defn slurp-code
  [file]
  (-> file
      (slurp)
      (s/replace #"jayzawro" "<username>")))

(def content
  [:div.guide
   [:div.inner
    [:section.what
     [:h2
      [:i.fad.fa-comet
       {:style {:font-size "1.5em"
                :display "block"}}]
      " Hello"]
     [:p.p
      "This page was served by a Clojure script running through "
      [:a
       {:href "https://github.com/babashka/babashka"}
       "Babashka"]
      ". This gives us the functional programming paradigm we love with the accessibility of PHP!"]]
    [:section.how-it-works
     [:h3
      [:i.fad.fa-project-diagram]
      " How it Works"]
     [:p.p
      "Your browser is requesting this file from the server. The server, running Apache, is processing it as a cgi-bin script. The script's contents is piped as input to the static "
      [:a {:href "https://github.com/babashka/babashka"}
       "Babashka"]
      " binary, which then interprets the code, runs the program, and outputs the printed HTML. The Apache web server then returns the HTML as a response which the browser pieces together into the page you are seeing now."]]
    [:section.the-code
     [:h3
      [:i.fad.fa-file-alt]
      " The Code"]
     [:h4 [:pre "metal.clj"]]
     [:p.p "Initial setup code. Pulls in libraries and adds source directories to source path. Might be able to make a generic bash script for this but works for now."]
     [:pre
      {:style {:margin-bottom "2rem"}}
      [:code.language-clojure.hls (slurp-code "metal.clj")]]
     [:h4 [:pre "metal/core.clj"]]
     [:p.p
      "The example code. Fetches each band from the database and displays them in HTML using the hiccup library that comes with "
      [:a {:href "https://github.com/babashka/babashka/blob/master/CHANGELOG.md#v028"}
       "Babashka 2.8"]
      "."]
     [:pre
      {:style {:margin-bottom "2rem"}}
      [:code.language-clojure.hls (slurp-code "metal/core.clj")]]]
    [:section.questions
     [:h3
      [:i.fad.fa-question-circle]
      " Questions"]
     [:div.question
      [:h4 "Yes but why?"]
      [:p.p "Writing code is more than just a language's features and popularity. It's how it all comes together, it's how it feels to do the day-to-day tasks, and the thought and care put into that is what makes Clojure such a fun language to work with. The forms just flow, I'm not buried in docs, and the libraries make use of common data structures that are often inherently compatible with each other. Now hacking together a webapp can leverage Clojure even on the cheapest of hosting providers if they allow cgi-scripts!"]]
     [:div.question
      [:h4 "Do I have to build a metal genre sampler with this too?"]
      [:p.p "Probably not!"
       " "
       [:i.fad.fa-guitar-electric
        {:style {:font-size "1.25em"}}]]]
     [:div.question
      [:h4 "Where can I learn more?"]
      [:p.p
       "Check out my article to explain the process and rationale at "
       [:br]
       [:i.fad.fa-file-alt]
       " "
       [:a
        {:href "https://eccentric-j.com/blog/clojure-like-its-php.html"}
        " https://eccentric-j.com/blog/clojure-like-its-php.html"]]
      [:p.p
       "Check out this example repo on github at "
       [:br]
       [:i.fab.fa-github]
       " "
       [:a
        {:href "https://github.com/eccentric-j/clj-cgi-example"}
        " https://github.com/eccentric-j/clj-cgi-example"]]]]
    [:section.credits
     {:style {:text-align "center"
              :margin-top "4rem"}}
     [:h3
      [:i.fad.fa-megaphone]
      " Thanks to"]
     [:p.p
      "A huge amount of credit goes to "
      [:a {:href "https://elais.codes/about.html"} "Taco"]
      " for figuring out tools like Clojure, Babashka, and cgi-scripts can be combined. Also thanks to rushsteve1 for making the problem approachable, borkdude for all your work on babashka and support for running it on a shared host, and didibus who helped me debug the first test script."]
     [:div
      {:style {:display "flex"
               :flex-flow "row nowrap"
               :justify-content "space-between"
               :text-align "center"}}
      [:div
       {:style {:flex "1 1 48%"
                :margin-bottom "2rem"}}
       [:h4
        [:i.fab.fa-discord]
        " Doom Emacs Discord"]
       [:ul.credits
        [:li
         [:strong [:a
                   {:href "https://elais.codes/about.html"}
                   "Taco"]]]
        [:li
         [:strong
          [:a {:href "https://rushsteve1.us"}
           "rushsteve1"]]]]]
      [:div
       {:style {:flex "1 1 48%"}}
       [:h4
        [:i.fab.fa-slack]
        " Clojurians Slack"]
       [:ul.credits
        [:li
         [:strong
          [:a {:href "https://michielborkent.nl"}
           "Borkdude"]]]
        [:li
         [:strong
          [:a {:href "https://rubberducking.com/"}
           "Didibus"]]]]]]
     [:div.hacking
      [:h3
       {:style {:border "none"
                :padding 0}}
       "Happy Hacking! "
       [:i.fad.fa-laptop-code]]]
     [:div.made-by
      {:style {:position "fixed"
               :right "1rem"
               :bottom "1rem"
               :border-radius (rems 16)
               :background "rgba(45, 36, 45, 0.8)"
               :padding (rems 10)
               :z-index 200}}
      [:span
       {:style {:color "#CC79B0"
                :font-family "Sriracha, cursive"}}
       "Made by"]
      [:a
       {:href "https://eccentric-j.com"}
       [:img
        {:src "/img/eccentric-j-logo.svg"
         :title "Made by Eccentric J"
         :alt "Eccentric J"
         :style {:display "block"
                 :height "auto"
                 :margin "0 auto"
                 :width (rems 120)}}]]
      [:span
       {:style {:display "block"
                :padding "3px 8px"
                :font-size (rems 14)
                :font-style "italic"
                :color "rgba(180, 180, 180, 1.0)"}}
       "Powered by jaunty narcissism"]]]]])
