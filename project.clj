(defproject ling "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.0-beta1"]
                 [org.clojure/clojure-contrib "1.2.0-beta1"]
                 [compojure "0.5.2"]
                 [ring/ring-jetty-adapter "0.3.1"]
                 [ring-json-params "0.1.1"]
                 [clj-json "0.3.1"]
                 [org.clojars.gilesc/stanford-parser "1.6.2"]]
  :dev-dependencies [[swank-clojure "1.2.1"]
                     [lein-run "1.0.0"]])
