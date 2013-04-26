(ns ling.launcher
  (:gen-class)
  (:use compojure.core ring.adapter.jetty)
  (:require [ling.core :as ling]
            [ling.conf :as conf]
            [ling.words :as words]))

(defn bootstrap-database
  "Obtain frequency data, parse it, and load it into the database."
  []
  (spit conf/frequencies-filename (slurp conf/frequencies-url))
  (-> @conf/db (.save-data (conf/parse-frequencies))))

(defn -main
  "Start jetty on a separate thread and serve the API."
  [& args]
  (if-not (-> @conf/db .exists?)
    (bootstrap-database))
  (words/init-word-freqs)
  (println "Running server on port" conf/port-number)
  (future (run-jetty (var ling/app) {:port conf/port-number})))
