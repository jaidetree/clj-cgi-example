#!/usr/bin/env bb

(ns depjar.depjar
  (:require
   [babashka.deps :as deps]
   [babashka.fs :as fs]
   [clojure.string :as s]
   [clojure.java.shell :refer [sh]]))

;; (def temp-dir (str (fs/create-temp-dir {:prefix "depjar"})))

(def deps-template
  {:paths []
   :deps {}
   :aliases {:remove-clojure
             {:classpath-overrides
              {'src/src nil
               'org.clojure/clojure nil
               'org.clojure/spec.alpha nil
               'org.clojure/core.specs.alpha nil}}}})

(defn temp-dir
  []
  (str (fs/create-temp-dir {:prefix "depjar"})))


(defn create-deps-edn
  "Create a deps.edn file using deps-template"
  [[dep recipe]]
  (assoc-in deps-template [:deps dep] recipe))

(defn save-deps-file!
  "
  Writes deps to a deps.edn file in a temp-dir
  Takes the dir and deps hash-map
  Returns nil
  "
  [dir deps]
  (spit (str (fs/path dir "deps.edn")) (pr-str deps)))

(defn classpath
  "Use the Clojure CLI to install deps and generate the classpath string"
  [dir]
  (-> (sh "bb" "--clojure" "-Aremove-clojure" "-Spath"
          :dir dir)
      (:out)
      (s/trim)
      (s/replace #":+" ":")))


(defn uberjar
  "Uberjar the deps based on the classpath"
  [dir classpath]
  (-> (sh "bb" "-cp" classpath "--uberjar" "package.jar"
          :dir dir)
      (:out)
      (s/trim)))

(defn mvjar
  "Moves the jar name based on the library basename"
  [dir jar-name]
  (let [source-path (str (fs/path dir "package.jar"))
        dest-path (str (fs/path "." jar-name) ".jar")]
    (fs/move source-path dest-path {:replace-existing true})))

(defn clean
  "Deletes the temp-dir we created to be polite"
  [dir]
  (fs/delete-tree dir))

(defn create-jar
  "
  Takes a deps-edn hash-map
  - Writes it to a temp-dir
  - Calculates the classpath
  - Creates an uberjar
  - Moves it to current directory
  - Deletes temp-dir
  "
  [deps-edn]
  (let [dir (temp-dir)
        deps (:deps deps-edn)
        dep (first deps)
        jar-name (fs/file-name (str (first dep)))]
    (println (str "Creating an uberjar for dep " (into {} deps)))
    (save-deps-file! dir deps-edn)
    (println (str "  -> installing deps"))
    (let [cp (classpath dir)]
      (println (str "  -> creating uber jar"))
      (uberjar dir cp)
      (println (str "  -> moving " jar-name ".jar"))
      (mvjar dir jar-name)
      (println (str "  -> cleaning tmpdir"))
      (clean dir))))

(defn create-jars
  "
  Create jars for each dependency

  Takes a string pointing to a deps.edn file

  Returns nil, many side-effects
  "
  [file]
  (println "Reading deps from" file)
  (let [deps (read-string (slurp file))]
    (->> deps
         (:deps)
         (map create-deps-edn)
         (map create-jar)
         (doall))
    (println "Done.")))

(defn help?
  "Is the user trying to run a help command?"
  [first-arg]
  (or (= first-arg "--help")
      (= first-arg "-h")))

(defn help
  "Display help text"
  []
  (println "

Dep Jar:
------------------------------------------------------------------------------

Makes jars out of each dependency so to share them across projects in a shared
hosting environment

Usage:

  depjar.clj [DEPS-FILE | --help]

    - DEPS-FILE
      Path to a deps.edn file
"))


(defn errors
  []
  (println "\nERROR: Invalid arguments")
  (help)
  (System/exit 1))

(defn -main
  "
  Takes arguments, validates them and dispatches target command
  "
  [[file & args]]
  (let [file (or file "deps.edn")]
    (cond (help? file)        (help)
          (fs/exists? file)  (create-jars file)
          :else               (errors))))

(-main *command-line-args*)

;; Required to not print last return value
(System/exit 0)
