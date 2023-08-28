(ns member
  (:require [coast]
            [buddy.hashers :as hashers]))


(defn build [request]
  (coast/form-for ::create
    [:input {:type "text" :name "member/email"}]
    [:input {:type "password" :name "member/password"}]
    [:input {:type "submit" :value "Submit"}]))


(defn create [request]
  (let [[_ errors] (-> (:params request)
                       (select-keys [:member/email :member/password])
                       (coast/validate [[:email [:member/email]
                                        [:required [:member/email :member/password]]]])
                       (update :member/password hashers/derive)
                       (coast/rescue))]
    (if (some? errors)
      (build (merge errors request))
      (-> (coast/redirect-to ::dashboard)
          (assoc :session (select-keys (:params request) [:member/email]))))))


(defn dashboard [request]
  [:div 
   [:h1 "You're signed in! Welcome!"]
   (coast/form-for :session/delete
    [:input {:type "submit" :value "Sign out"}])])
