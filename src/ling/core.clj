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
       (json-response (vals (words/sort-words string "asc"))))

  (PUT "/words/sort/desc" [string]
       (json-response (vals (words/sort-words string "desc"))))

  (PUT "/words/interesting/least" [string count]
       (json-response (words/least-interesting-words string (Integer. count))))

  (PUT "/words/interesting/most" [string count]
       (json-response (words/most-interesting-words string (Integer. count))))

  (PUT "/sentences/sort/asc" [string]
       (json-response (vals (words/sort-sentences string "asc"))))

  (PUT "/sentences/sort/desc" [string]
       (json-response (vals (words/sort-sentences string "desc"))))

  (PUT "/sentences/interesting/least" [string count]
       (json-response (words/least-interesting-sentences string (Integer. count))))

  (PUT "/sentences/interesting/most" [string count]
       (json-response (words/most-interesting-sentences string (Integer. count)))))

(def app
     (-> handler
         wrap-json-params))
