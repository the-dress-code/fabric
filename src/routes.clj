(ns routes
  (:require [coast]
            [components]
            [middleware]))


(def routes
  (coast/routes

    (coast/site

     [:get "/sign-up" :member/build]
     [:post "/members" :member/create]
     [:get "/sign-in" :session/build]
     [:post "/sessions" :session/create]

     (coast/with middleware/auth

        [:get "/dashboard" :member/dashboard]
        [:delete "/sessions" :session/delete])

     (coast/with-layout components/layout
        [:get "/" :site.home/index]
        [:get "/fabrics/search" :fabric/search]
        [:get "/fabrics/answers" :fabric/answers]
        [:resource :fabric]))

    (coast/api

      (coast/with-prefix "/api"
        [:get "/" :api.home/index]))))
