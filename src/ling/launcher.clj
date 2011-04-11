(ns ling.launcher
   (:use compojure.core ring.adapter.jetty)
   (:require [ling.core :as ling]))

; start jetty on a separate thread
(future (run-jetty (var ling/app) {:port 8080}))
