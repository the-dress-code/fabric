(ns server
  (:require [coast]
            [routes])
  (:gen-class))

(defn app 
 []
  (coast/app {:routes (routes/routes)}))

(defn -main [& [port]]
  (coast/server app {:port port}))

(comment
  (-main))
