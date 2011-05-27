(ns ling.launcher
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
  []
  (if-not (-> @conf/db .exists?)
    (bootstrap-database))
  (words/init-word-freqs)
  (future (run-jetty (var ling/app) {:port conf/port-number})))
