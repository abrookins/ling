(defproject ling "1.0.5-SNAPSHOT"
  :description "Rank words and sentences based on originality within a corpus."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [ring/ring-json "0.2.0"]
                 [clj-json "0.5.3"]
                 [org.clojars.gilesc/stanford-parser "1.6.2"]],
  :main ling.launcher)
