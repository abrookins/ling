(ns ling.database.file
  "A database that saves and loads data to and from a file on disk."
  (:require [ling.database.base :as base])
  (:import [java.io File]))

(defrecord FileDatabase [filename]
  base/DatabaseIO
  (exists? [this]
    (-> (File. (:filename this)) .exists))
  (save-data [this data]
    (spit (:filename this) (pr-str data)))
  (load-data [this]
    (read-string (slurp (:filename this)))))

(defn make-file-database
  "Factory method to create a FileDatabase."
  [filename]
  (FileDatabase. filename))
