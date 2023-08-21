(ns routes
  (:require [coast]
            [components]))

(def routes
  (coast/routes

    (coast/site
      (coast/with-layout components/layout
        [:get "/" :site.home/index]
        [:get "/fabrics/search" :fabric/search]
        [:resource :fabric]))

    (coast/api
      (coast/with-prefix "/api"
        [:get "/" :api.home/index]))))
