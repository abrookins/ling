(ns ling.core
  (:use compojure.core ring.middleware.json-params)
  (:require [ling.words :as words]
            [clj-json.core :as json]
            [compojure.route :as route])
  (:import edu.stanford.nlp.process.DocumentPreprocessor))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes handler
  (GET "/" []
       "<h1>Originality API Status</h1><p>Online!</p>")

  (PUT "/words/originality" [string]
       (if-not (empty? string)
         (json-response
          (words/originality string @words/all-words (DocumentPreprocessor.)))))

  (PUT "/words/sort/asc" [string]
       (if-not (empty? string)
         (let [processor (DocumentPreprocessor.)]
           (json-response
            (vals (words/sort-strings-asc
                   (words/words string processor) @words/all-words processor))))))

  (PUT "/words/sort/desc" [string]
       (if-not (empty? string)
         (let [processor (DocumentPreprocessor.)]
           (json-response
            (vals (words/sort-strings-desc
                   (words/words string processor) @words/all-words processor))))))

  (PUT "/words/interesting/least" [string count]
       (if-not (empty? string)
         (let [processor (DocumentPreprocessor.)]
           (json-response
            (words/least-interesting
             (Integer. count) (words/words string processor) @words/all-words processor)))))

  (PUT "/words/interesting/most" [string count]
       (if-not (empty? string)
         (let [processor (DocumentPreprocessor.)]
           (json-response
            (words/most-interesting
             (Integer. count) (words/words string processor) @words/all-words processor)))))

  (PUT "/sentences/sort/asc" [string]
       (if-not (empty? string)
         (let [processor (DocumentPreprocessor.)]
           (json-response
            (vals
             (words/sort-strings-asc
              (words/sentences string processor) @words/all-words processor))))))

  (PUT "/sentences/sort/desc" [string]
       (if-not (empty? string)
         (let [processor (DocumentPreprocessor.)]
           (json-response
            (vals
             (words/sort-strings-desc
              (words/sentences string processor) @words/all-words processor))))))

  (PUT "/sentences/interesting/least" [string count]
       (if-not (empty? string)
         (let [processor (DocumentPreprocessor.)]
           (json-response
            (words/least-interesting
             (Integer. count) (words/sentences string processor) @words/all-words processor)))))

  (PUT "/sentences/interesting/most" [string count]
       (if-not (empty? string)
         (let [processor (DocumentPreprocessor.)]
           (json-response
            (words/most-interesting
             (Integer. count) (words/sentences string processor) @words/all-words processor))))))

(def app
     (-> handler
         wrap-json-params))
