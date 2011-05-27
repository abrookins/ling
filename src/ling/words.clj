(ns ling.words
  (:require [ling.conf :as conf]
            [clojure.contrib.generic.math-functions :as math]
            [clojure.contrib.string :as str])
  (:import edu.stanford.nlp.process.DocumentPreprocessor))

(def ^{:doc "A processor for chunking words from strings."}
  processor (atom (DocumentPreprocessor.)))

(def ^{:doc "A map of words to frequency ratios."}
  word-freqs (atom '()))

(defn init-word-freqs
  "Load the saved map of words and frequency ratios."
  []
  (if (-> @conf/db .exists?)
    (swap! word-freqs (fn [_] (-> @conf/db .load-data)))))

(defn words
  "Convert a string to a collection of words."
  [input]
  (if (= (type input) java.lang.String)
    ;; .getWordsFromString returns a List of Stanford NLP Word objects.
    (map (fn [word] (-> word .word)) (.getWordsFromString @processor input))
    (identity input)))

(defn sentences
  "Convert a string to a collection of sentences."
  [string]
  (map (fn [sentence] (map (fn [word] (-> word .word)) sentence))
       (.getSentencesFromText @processor (java.io.StringReader. string))))

(defn get-frequency
  "Try to get a word's frequency ratio from the database. Try the word first,
  then the word as lowercase. Return 0 if the word does not exist."
  [word]
  (let [freq (get @word-freqs word 0)]
    (if (= 0 freq)
      (get @word-freqs (str/lower-case word) 0)
      freq)))

(defn rank-word
  "Return the log of the frequency ratio of a word if the word exists in
  the database; otherwise, return zero."
  [word]
  (let [rank (get-frequency word)]
    (if-not (zero? rank)
      (math/log rank)
      0)))

(defn count-word-matches
  "Given a list of words, return the number for which the word database had
  a non-zero frequency ratio."
  [words]
  (reduce + (map (fn [word] (if (zero? (get-frequency word)) 0 1)) words)))

(defn originality
  "Calculate the 'originality' of a string by finding the average frequency
  ratio of words parsed from the string."
  [string]
  (let [hits (count-word-matches (words string))
        rank-sum (reduce + (map (fn [word] (rank-word word)) (words string)))]
    (if (zero? hits)
      (assoc {} :originality 0 :accuracy 0)
      (assoc {}
        :originality (/ rank-sum hits)
        :accuracy (/ 1.0 (/ (count (words string)) hits))))))

(defn sort-strings-desc
  "Rank and sort strings in descending order by originality."
  [strings-to-sort]
  (into (sorted-map-by (fn [key1 key2] (> key1 key2)))
        (map (fn [string] (let [orig (:originality (originality string))]
                (if-not (zero? orig)
                  (hash-map orig string))))
             strings-to-sort)))

(defn sort-strings-asc
  "Rank and sort text in ascending order by originality."
  [strings-to-sort]
  (into (sorted-map)
        (map (fn [string] (let [orig (:originality (originality string))]
                (if-not (zero? orig)
                  (hash-map orig string))))
             strings-to-sort)))

(defn top
  "Get top n strings from a list of strings after sorting with sort-fn."
  [n strings-to-rank sort-fn]
  (if (or (empty? strings-to-rank) (nil? n))
    '()
    (let [ranks (sort-fn strings-to-rank)]
      (reverse (into '() (map (fn [rank] (val rank)) (take n ranks)))))))

(defn partition-sort
  "Sort a list of strings after partitioning with part-fn. Takes 'asc' or 'desc'
  for direction parameter (for ascending or descending sort)."
  [part-fn string direction]
  (case direction
    "asc" (sort-strings-asc (part-fn string))
    "desc" (sort-strings-desc (part-fn string))))

(defn most-interesting
  "Partition a string into a list of strings with part-fn and get the n most
  interesting strings from the list."
  [n part-fn strings-to-rank]
  (top n (part-fn strings-to-rank) sort-strings-asc))

(defn least-interesting
  "Partition a string into a list of strings with part-fn and get the n least
  interesting strings from the list."
  [n part-fn strings-to-rank]
  (top n (part-fn strings-to-rank) sort-strings-desc))

;; API

(defn most-interesting-words
  "Parse a string into a list of words and find the n most interesting words
  in the list."
  [string n]
  (most-interesting n words string))

(defn least-interesting-words
  "Parse a string into a list of words and find the n least interesting words
  in the list."
  [string n]
  (least-interesting n words string))

(defn most-interesting-sentences
  "Parse a string into a list of sentences and find the n most interesting
  sentences in the list."
  [string n]
  (most-interesting n sentences string))

(defn least-interesting-sentences
  "Parse a string into a list of sentences and find the n least interesting
  sentences in the list."
  [string n]
  (least-interesting n sentences string))

(defn sort-words
  "Parse a string into a list of words and sort the list by direction, where
  direction is either 'asc' or 'desc'."
  [string direction]
  (partition-sort words string direction))

(defn sort-sentences
  "Parse a string into a list of sentences and sort the list by direction,
  where direction is either 'asc' or 'desc'."
  [string direction]
  (partition-sort sentences string direction))
