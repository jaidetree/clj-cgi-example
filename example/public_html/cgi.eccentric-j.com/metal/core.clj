(ns metal.core
  (:require
   [clojure.string :as s]
   [hiccup.util :refer [raw-string]]
   [honeysql.core :as sql]
   [pod.babashka.postgresql :as pg]
   [gaka.core :refer [css]]
   [metal.guide :as guide]
   [metal.style :refer [rems]]))

;; Load secrets for the db
(def secrets (read-string (slurp "prod.secret.edn")))

;; Connect to the database
(def db {:dbtype   "postgresql"
         :host     "localhost"
         :dbname   (:db/name secrets)
         :user     (:db/user secrets)
         :password (:db/password secrets)
         :port     5432})

;; Fetch bands from the database
(def bands (pg/execute! db (sql/format {:select [:*]
                                        :from [:metal_bands]
                                        :order-by [[:rank :desc]]})))

(defn embed-url
  "Transforms a public youtube URL to the embedded URL"
  [yt-url]
  (as-> yt-url $
    (s/split $ #"=")
    (drop 1 $)
    (s/join "" $)
    (str "https://youtube.com/embed/" $)))

(defn popularity
  [pop-rank]
  (str (s/join "" (repeat pop-rank "★"))
       (s/join "" (repeat (- 5 pop-rank) "☆"))))

(def style
  (css
      [:body
       {:padding 0
        :margin 0
        :font-family :sans-serif
        :background-color "#E5E5E5"}]
      [:h1
       {:font-size (rems 32)}]
      [:h2
       {:font-size (rems 24)
        :font-family "\"Metal Mania\", sans-serif"}]
      [:h3
       {:font-size (rems 20)}]
      [:h4
       {:font-size (rems 18)}]
      [:.example
       {:padding "2rem"
        :text-align :center
        :margin-bottom "4rem"}]
      [:.cards
       {:list-style "none"
        :display "flex"
        :flex-flow "row wrap"
        :align-items "stretch"
        :justify-content "space-evenly"
        :margin "0 auto"
        :padding "0"
        :max-width "1100px"}]
      [:.cards__item
       {:background-color "#FFF"
        :flex "0 0 320px"
        :box-sizing "border-box"
        :position "relative"
        :margin "1rem"
        :box-shadow "0 0 10px 0 rgba(0, 0, 0, 0.2)"}]
      [:.card
       {:box-sizing :border-box
        :width "320px"
        :display "block"
        :position :relative
        :text-align :left
        :border-bottom-left-radius "5px"
        :border-bottom-right-radius "5px"}]
      [:.rank
       {:position :absolute
        :right "-16px"
        :top "-16px"
        :z-index 100
        :border-radius "50%"
        :width "32px"
        :height "32px"
        :background "#fff"
        :line-height "32px"
        :text-align :center
        :font-size (rems 14)
        :font-style :italic
        :color "#666"}]
      [:.card__body
       {:padding "1rem"}]
      [:.detail
       {:margin-bottom "1rem"}]
      [:.label
       {:display "block"
        :font-weight "bold"}]
      [:.comment
       {:font-size (rems 14)
        :line-height 1.4}]))

(def example
  [:section.example
   [:h1 "Running Clojure as a CGI-bin Script Example"]
   [:h2 "Example Project"]
   [:p "A Metal Subgenre Sample Platter"]
   [:ul.cards
    (for [band bands]
      [:li.cards__item
       [:div.card
        [:span.rank
         (inc (- (count bands) (:metal_bands/rank band)))]
        [:iframe
         {:width "320"
          :height "180"
          :src (embed-url (:metal_bands/music_video band))
          :frameborder "0"
          :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          :allowfullscreen true}]
        [:div.card__body
         [:h3 (:metal_bands/name band)]
         [:div.detail
          [:span.label "Genre"]
          [:span (:metal_bands/genre band)]]
         [:div.detail
          [:span.label "Popularity"]
          [:span (popularity (:metal_bands/popularity band))]]
         [:div.detail
          [:span.label "Recommended Album"]
          [:span
           {:style {:font-style "italic"}}
           (:metal_bands/recommended_album band)]]
         [:p.comment
          (:metal_bands/comment band)]]]])]])

(def content
  [:html
   [:head
    [:title "Live Clojure CGI Script Example"]
    [:meta {:charset "UTF-8"}]
    [:link {:rel "preconnect"
            :href "https://fonts.gstatic.com"}]
    [:link {:rel "stylesheet"
            :href "https://fonts.googleapis.com/css2?family=Metal+Mania&family=Sriracha&display=swap"}]
    [:link {:rel "stylesheet"
            :href "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/styles/atelier-cave-dark.min.css"}]
    [:style (raw-string style)]
    [:style (raw-string guide/styles)]]
   [:body
    [:div#page
     example
     guide/content]
    [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/highlight.min.js"}]
    [:script {:src "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.13.1/languages/clojure.min.js"}]
    [:script "hljs.initHighlightingOnLoad();"]
    ]])

