#!/bin/env /home1/<username>/bin/bb

(ns cgi.metal
  (:require
   [babashka.fs :as fs]
   [babashka.classpath :refer [add-classpath]]
   [babashka.pods :as pods]
   [clojure.string :as s]
   [hiccup2.core :refer [html]]))

;; Dynamic Libs
(def LIB-DIR "/home1/<username>/lib/")
(def CWD
  (if-let [filename (System/getenv "SCRIPT_FILENAME")]
    (str (fs/parent filename))
    (System/getenv "PWD")))

(defn lib
  "
  Create an absolute path to a jar file in sibling lib directory
  Takes a string filename like \"honeysql.jar\"
  Returns a string like \"/path/to/dir/lib/honeysql.jar\".
  "
  [path]
  (str LIB-DIR path))

;; Add jars and current directory to classpath to import library code

(add-classpath (s/join ":" [CWD
                            (lib "gaka.jar")
                            (lib "honeysql.jar")]))
(pods/load-pod (lib "pod-babashka-postgresql"))

;; Require our main page code
;; Must come after updating the class path

(require
 '[metal.core :as metal])

;; CGI scripts must print headers then body

(println "Content-type:text/html\r\n")
(println (str (html metal/content)))

(System/exit 0)
