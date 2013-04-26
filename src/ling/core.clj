(ns ling.core
  (:use compojure.core ring.middleware.json)
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

  (PUT "/words/sort/asc" [string limit]
       (json-response (words/sort-strings string :word :asc (Integer. limit))))

  (PUT "/words/sort/desc" [string limit]
       (json-response (words/sort-strings string :word :desc (Integer. limit))))

  (PUT "/sentences/sort/asc" [string limit]
       (json-response (words/sort-strings string :sentence :asc (Integer. limit))))

  (PUT "/sentences/sort/desc" [string limit]
       (json-response (words/sort-strings string :sentence :desc (Integer. limit)))))

(def app
     (-> handler
         wrap-json-params))
