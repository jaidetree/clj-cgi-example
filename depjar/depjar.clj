#!/usr/bin/env bb

(ns depjar.depjar
  (:require
   [clojure.string :as s]
   [clojure.java.shell :refer [sh]]))

(def deps-template
  {:paths []
   :deps {}
   :aliases {:remove-clojure
             {:classpath-overrides
              {'src/src nil
               'org.clojure/clojure nil
               'org.clojure/spec.alpha nil
               'org.clojure/core.specs.alpha nil}}}})

(def alphanum (vec (s/split "abcdefghijklmnopqrstuvwxyz01234567890" #"")))

(defn rand-dir
  "Generate a random temporary directory name with letters a-z and 0-9"
  []
  (s/join "" (repeatedly 6 #(rand-nth alphanum))))

(defn tmpdir
  "Form a temporary dir using a randomized key"
  [key]
  (str ".depjar-" key))

(defn mktmpdir
  "Make a temporary directory from a string key, typically random"
  [dir]
  (sh "mkdir" (tmpdir dir)))

(defn update-deps
  "Updates deps hash-map with a artifact and version in :deps"
  [deps-edn artifact version]
  (assoc-in deps-edn [:deps (symbol artifact)] {:mvn/version version}))

(defn save-deps!
  "Saves our deps hash-map to our tmpdir deps.edn"
  [dir deps-edn]
  (spit (str (tmpdir dir) "/deps.edn") (pr-str deps-edn)))

(defn classpath
  "Use the Clojure CLI to install deps and generate the classpath string"
  [dir]
  (-> (sh "clojure" "-Aremove-clojure" "-Spath"
          :dir (tmpdir dir))
      (:out)
      (s/trim)))

(defn uberjar
  "Create the uberjar from the classpath"
  [dir classpath]
  (-> (sh "bb" "-cp" classpath "--uberjar" "package.jar"
          :dir (tmpdir dir))
      (:out)))

(defn mvjar
  "Move the package.jar to the output argument"
  [dir output-path]
  (sh "mv" (str (tmpdir dir) "/package.jar") output-path))

(defn rmtmpdir
  "Clean up the directory we created to create the uberjar"
  [dir]
  (sh  "rm" "-r" (tmpdir dir)))

(defn create-jar
  "
  Main pipeline to create a jar file of a specific artifact from clojars

  Takes the following args:
  - an artifact symbol like 'honeysql/honeysql
  - A version string like \"1.0.5\"
  - Output file like \"honeysql.jar\"

  Returns nil, many side-effects
  "
  [artifact version output]
  (let [dir (rand-dir)
        deps (update-deps deps-template artifact version)]
    (println (str "Creating an uberjar for [" artifact " " version "]"))
    (println (str "  -> making tmpdir"))
    (mktmpdir dir)
    (println (str "  -> writing deps.edn"))
    (save-deps! dir deps)
    (println (str "  -> installing deps"))
    (let [cp (classpath dir)]
      (println (str "  -> creating uber jar"))
      (uberjar dir cp)
      (println (str "  -> moving jar to " output))
      (mvjar dir output)
      (println (str "  -> cleaning tmpdir"))
      (rmtmpdir dir)
      (println "Done."))))

(defn valid-args?
  "Validate arguments slightly"
  [[artifact version output :as args]]
  (and (= (count args) 3)
       (some? (re-find #"[-_.a-z0-9]+/[-_.a-z0-9]+" artifact))
       (some? (re-find #"\d{1,2}\.\d{1,2}\.[-A-Z0-9]+" version))
       (some? output)))

(defn help?
  "Is the user trying to run a help command?"
  [[first-arg]]
  (or (= first-arg "--help")
      (= first-arg "-h")))

(defn help
  "Display help text"
  []
  (println "
Usage:

  depjar.clj ARTIFACT VERSION OUTPUT

    - ARTIFACT
      A qualified coordinate to a clojar library like honeysql/honeysql

    - VERSION
      A full semver version string like 1.0.5

    - OUTPUT
      Name of the output jarfile like honeysql.jar
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
  [args]
  (let [args (map s/trim args)
        is-valid (valid-args? args)]
    (cond (help? args)         (help)
          is-valid             (apply create-jar
                                      (symbol (first args))
                                      (rest args))
          :else                (errors))))

(-main *command-line-args*)

;; Required to not print last return value
(System/exit 0)
