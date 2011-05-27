(defproject ling "1.0.5-SNAPSHOT"
  :description "Rank words and sentences based on originality within a corpus."
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [compojure "0.5.2"]
                 [ring/ring-jetty-adapter "0.3.1"]
                 [ring-json-params "0.1.1"]
                 [clj-json "0.3.1"]
                 [org.clojars.gilesc/stanford-parser "1.6.2"]]
  :dev-dependencies [[swank-clojure "1.2.1"]
                     [lein-run "1.0.0"]])
