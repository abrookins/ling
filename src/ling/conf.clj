(ns ling.conf
  (:require [ling.parsers :as parsers]
            [ling.database.file :as file-db]))

(def frequencies-filename
  "The name of the file from which to load raw frequency data."
  "ANC-token-word.txt")

(def frequencies-url
  "The URL from which to download the raw word frequency data."
  (str "http://www.americannationalcorpus.org/SecondRelease/data/" frequencies-filename))

(def parse-frequencies
  "The parser to use for obtaining words and word frequency ratios."
  (fn [] (parsers/parse-tokenized-anc-freqs frequencies-filename)))

(def db-name
  "The name of the database in which to store word frequency ratios."
  "db.clj")

(def db
  "The database to use for persisting word and frequency data."
  (atom (file-db/make-file-database db-name)))

(def port-number
  "The port number on which to run the web service."
  8080)

