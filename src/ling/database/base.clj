(ns ling.database.base
  "Base protocols for defining persistent key-value stores.")

(defprotocol DatabaseIO
  "Input/output operations for a database."
  (exists? [this] "Whether or not this database exists.")
  (save-data [data this] "Save data to a database.")
  (load-data [this] "Load data from a database."))
