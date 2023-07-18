(ns site.home
  (:require [coast]))


(defn index [request]
  [:h1 {:class "tc"}
   "You're coasting on clojure!"])
