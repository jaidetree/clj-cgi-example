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
    [:a
     {:color "#7DDAAB"
      :transition "all 250ms ease-in-out"
      :text-decoration "none"}]
    [:a:hover
     {:color "#CC79B0"
      :text-decoration "underline"}]
    ["h2, h3"
     {:font-family "Sriracha, cursive"}]
    [:h2
     {:color "#7DDAAB"}]
    [:h3
     {:color "#CC79B0"}]
    [:.inner
     {:max-width "800px"
      :margin "0 auto"}]
    [:.p
     :font-size (rems 20)
     :line-height 1.8]
    [:.how-it-works
     {:border-top "1px solid #7DDAAB"}]
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
      (s/replace #"<actual-username>" "<username>")))

(def content
  [:div.guide
   [:div.inner
    [:section.how-it-works
     [:h2 "How it Works"]
     [:p.p
      "Your browser is requesting this file from the server. The server, running Apache, is processing it as a cgi-bin script. The script's contents is piped as input to the static "
      [:a {:href "https://github.com/babashka/babashka"}
       "Babashka"]
      " binary, which then interprets the code, runs the program, and outputs the printed HTML. The Apache web server then returns the HTML as a response which the browser pieces together into the page you are seeing now."]]
    [:section.the-code
     [:h2 "The Code"]
     [:h3 [:pre "metal.clj"]]
     [:p.p "Initial setup code. Pulls in libraries and adds source directories to source path. Might be able to make a generic bash script for this but works for now."]
     [:pre
      {:style {:margin-bottom "2rem"}}
      [:code.language-clojure.hls (slurp-code "metal.clj")]]
     [:h3 [:pre "metal/core.clj"]]
     [:p.p
      "The example code. Fetches each band from the database and displays them in HTML using the hiccup library that comes with "
      [:a {:href "https://github.com/babashka/babashka/blob/master/CHANGELOG.md#v028"}
       "Babashka 2.8"]
      "."]
     [:pre
      {:style {:margin-bottom "2rem"}}
      [:code.language-clojure.hls (slurp-code "metal/core.clj")]]]
    [:section.questions
     [:h2 "Questions"]
     [:div.question
      [:h3 "Yes but why?"]
      [:p.p "Writing code is more than just a language's features and popularity. It's how it all comes together, it's how it feels to do the day-to-day tasks, and the thought and care put into that is what makes Clojure such a fun language to work with. The forms just flow, I'm not buried in docs, and the libraries make use of common data structures that are often inherently compatible with each other. Now hacking together a webapp can leverage Clojure even on the cheapest of hosting providers if they allow cgi-scripts!"]]
     [:div.question
      [:h3 "Do I have to build a metal genre sampler with this too?"]
      [:p.p "Probably not!"]]
     [:div.question
      [:h3 "Where can I learn more?"]
      [:p.p
       "Check out my article to explain the process and rationale at "
       [:br]
       [:a {:href "https://eccentric-j.com/blog/clojure-like-its-php.html"}
        "https://eccentric-j.com/blog/clojure-like-its-php.html"]]
      [:p.p
       "Check out this example repo on github at "
       [:br]
       [:a {:href "https://github.com/eccentric-j/clj-cgi-example"}
        "https://github.com/eccentric-j/clj-cgi-example"]]]]
    [:section.credits
     {:style {:text-align "center"
              :margin-top "4rem"}}
     [:h2
      "Thanks to"]
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
       [:h3 "Doom Emacs Discord"]
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
       [:h3 "Clojurians Slack"]
       [:ul.credits
        [:li
         [:strong
          [:a {:href "https://michielborkent.nl"}
           "Borkdude"]]]
        [:li
         [:strong
          [:a {:href "https://rubberducking.com/"}
           "Didibus"]]]]]]
     [:h2
      "Made by"]
     [:a
      {:href "https://eccentric-j.com"}
      [:img
       {:src "/img/eccentric-j-logo.svg"
        :width "50%"
        :alt "Eccentric J"
        :style {:height "auto"}}]]]]])
