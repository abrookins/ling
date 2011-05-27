(ns ling.parsers
  (:require [clojure.contrib.duck-streams :as streams]
            [clojure.contrib.str-utils :as str-utils]))

(defn parse-tokenized-anc-freqs
  "Parse word and frequency data from a tab-delimited file of American National
  Corpus tokenized frequency data (not the all-lemmas file). The format of a row
  of data should look like the following:

  abridged    9   4.0604587E-7

  Where 'abridged' is the word, 9 is the number of times the word appeared in
  the corpus, and 4.0604587E-7 is the word frequency ratio."
  [filename]
  (into {}
        (map (fn [row] (let [[word _ frequency-ratio] (str-utils/re-split #"\t" row)]
                (hash-map word
                      (if-not (empty? frequency-ratio)
                      ,(Float. frequency-ratio)
                      0))))
             (streams/read-lines filename))))
