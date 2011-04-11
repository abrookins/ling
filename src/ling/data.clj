(ns ling.data)

(defn save-data 
  "Save data to a file."
  [data filename]
  (spit filename (pr-str data)))

(defn load-data
  "Load data from a file."
  [filename]
  (read-string (slurp filename)))
