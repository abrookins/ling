(ns ling.core
  (:use compojure.core ring.middleware.json-params)
  (:require [ling.words :as words]
            [clj-json.core :as json]
            [compojure.route :as route]))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes handler
  (GET "/" []
       "<h1>Word Originality API Status</h1><p>Online!</p>")
  
  (PUT "/words/originality" [string]
       (json-response (words/originality string)))

  (PUT "/words/sort/asc" [string]
       (json-response
             (vals (words/sort-strings-asc (words/words string)))))

  (PUT "/words/sort/desc" [string]
       (json-response
             (vals (words/sort-strings-desc (words/words string)))))

  (PUT "/words/interesting/least" [string count]
       (json-response
            (words/least-interesting (Integer. count) (words/words string))))

  (PUT "/words/interesting/most" [string count]
       (json-response
             (words/most-interesting (Integer. count) (words/words string))))

  (PUT "/sentences/sort/asc" [string]
       (json-response
             (vals (words/sort-strings-asc (words/sentences string)))))

  (PUT "/sentences/sort/desc" [string]
       (json-response
             (vals (words/sort-strings-desc (words/sentences string)))))

  (PUT "/sentences/interesting/least" [string count]
       (json-response
             (words/least-interesting (Integer. count) (words/sentences string))))

  (PUT "/sentences/interesting/most" [string count]
       (json-response
            (words/most-interesting (Integer. count) (words/sentences string)))))

(def app
     (-> handler
         wrap-json-params))
