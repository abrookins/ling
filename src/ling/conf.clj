(ns ling.conf
  (:require [ling.parsers :as parsers]
            [ling.database.file :as file-db]))

(def ^{:doc "The name of the file from which to load raw frequency data."}
  frequencies-filename "ANC-token-word.txt")

(def ^{:doc "The URL from which to download the raw word frequency data."}
  frequencies-url (str "http://www.americannationalcorpus.org/SecondRelease/data/" frequencies-filename))

(def ^{:doc "The parser to use for obtaining words and word frequency ratios."}
  parse-frequencies (fn [] (parsers/parse-tokenized-anc-freqs frequencies-filename)))

(def ^{:doc "The name of the database in which to store word frequency ratios."}
  db-name "db.clj")

(def ^{:doc "The database to use for persisting word and frequency data."}
  db (atom (file-db/make-file-database db-name)))

(def ^{:doc "The port number on which to run the web service."}
  port-number 8080)

