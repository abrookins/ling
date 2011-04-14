(ns ling.words
  (:require [ling.data :as data]
            [clojure.contrib.generic.math-functions :as math]
            [clojure.contrib.string :as str]
            [clojure.contrib.duck-streams :as streams]
            [clojure.contrib.str-utils :as str-utils])
  (:import edu.stanford.nlp.process.DocumentPreprocessor))

;; Our database of word frequencies. 
(def db-filename "db.clj")

(def word-db (atom (data/load-data db-filename)))

;; A processor for chunking words from strings.
(def processor (atom (DocumentPreprocessor.)))

(defn load-words
     "Load tab-delimited word data from a file."
     [filename]
     (into {}
           (map #(let [[word freq ratio] (str-utils/re-split #"\t" %)]
                   (hash-map word
                    (if-not (empty? ratio)
                      ,(Float. ratio)
                      0)))
                (streams/read-lines filename))))

(defn transform-word
  "Given a map of :word and :latest_rank, create a new map keyed
   on :word with the value :latest_rank."
  [word]
  (if (and 
        (contains? word :word)
        (contains? word :latest_rank)
        (not (nil? (get word :latest_rank))))
    (hash-map (word :word) (word :latest_rank))))

(defn flip-map
  "Given a map, return a new map with the values of the original map
  as the keys."
  [orig]
  (into (sorted-map) (zipmap (vals orig) (keys orig))))

(defn words
  "Convert a string to a collection of words."
  [input]
  (if (= (type input) java.lang.String)
    (map #(.word %) (.getWordsFromString @processor input))
    (identity input)))

(defn sentences
  "Convert a string to a collection of sentences."
  [string]
  (map #(map (fn [w] (.word w)) %) (.getSentencesFromText @processor (java.io.StringReader. string))))

(defn get-rank
  "Get a word's rank from a hash of words.  Try the word first,
  then the word as lowercase."
  [word]
  (let [rank (get @word-db word 0)]
    (if (= 0 rank)
      (get @word-db (str/lower-case word) 0)
      rank)))

(defn rank-word
  "Return the log of the search rank of a word if the word exists;
  otherwise, return zero."
  [word]
  (let [rank (get-rank word)]
    (if-not (zero? rank)
      (math/log rank)
      0)))

(defn count-word-matches
  "Return the number of matches between a list of words and non-zero ranks
  in the rank cache."
  [words]
  (reduce + (map #(if (zero? (get-rank %)) 0 1) words)))

(defn originality
  "Calculate the average frequency of words in a given string."
  [string]
  (let [hits (count-word-matches (words string)) rank-sum
        (reduce + (map #(rank-word %) (words string)))]
    (if (zero? hits)
      (assoc {} :score 0 :accuracy 0)
      (assoc {}
        :score (/ rank-sum hits)
        :accuracy (/ 1.0 (/ (count (words string)) hits))))))

(defn sort-strings-desc
  "Rank and sort strings in descending order by originality."
  [strings-to-sort]
  (into (sorted-map-by (fn [key1 key2] (> key1 key2)))
        (map #(let [score (:score (originality %))]
                (if-not (zero? score)
                  (hash-map score %)))
             strings-to-sort)))

(defn sort-strings-asc
  "Rank and sort text in ascending order by rank."
  [strings-to-sort]
  (into (sorted-map)
        (map #(let [score (:score (originality %))]
                (if-not (zero? score)
                  (hash-map score %)))
             strings-to-sort)))

(defn interesting
  "Get n interesting words by sorting with sort-fn."
  [n strings-to-rank sort-fn]
  (if (or (empty? strings-to-rank) (nil? n))
    '()
    (let [ranks (sort-fn strings-to-rank)]
      (reverse (into '() (map #(val %) (take n ranks)))))))

(defn most-interesting
  "Get the n most interesting strings from a set of strings."
  [n strings-to-rank]
  (interesting n strings-to-rank sort-strings-asc))

(defn least-interesting
  "Get the n least interesting words from a set of strings."
  [n strings-to-rank]
  (interesting n strings-to-rank sort-strings-desc))

(defn refresh-word-db
  "Load the saved hash of word ranks as an atom."
  []
  (swap! word-db (data/load-data db-filename)))

(defn find-originality
  "Find the originality of a string."
  [string]
  (originality (words string @processor)))
