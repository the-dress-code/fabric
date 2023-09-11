(ns member
  (:require [coast]
            [components :refer [thead th container input label select submit link-to table]]
            [buddy.hashers :as hashers]))


(defn build [request]
  (container {:mw 6}

   (thead "SIGN UP")

   (when-not (or (some? (:error/message request))
                 (some? (-> request
                           :params
                           :error))) 
      [:p [:br]])

   (when (some? (:error/message request))
                  [:div (:error/message request)])

   (coast/form-for ::create

    (label {:for "member/email"} "Email")
    (input {:type "text" :name "member/email" :required true})

    (label {:for "member/password"} "Password")
    (input {:type "password" :name "member/password" :required true})

    [:p {:style "color:gray;font-size:10px;"} "Password must start with a letter or special character."]

    [:Input {:class "input-reset pointer dim db bn f6 br2 ph3 pv2 dib white bg-blue"
             :type "submit" 
             :value "SIGN UP"}]

    [:P [:br] "Already a user? " (link-to (coast/url-for :session/build) "LOGIN")])))


(defn create [request]
  (let [email (get-in request [:params :member/email])
        member (coast/find-by :member {:email email})]
    (if member
      (coast/redirect-to :session/build {:error "dupe"})
      (let [[_ errors] (-> (:params request)
                           (select-keys [:member/email :member/password])
                           (coast/validate [[:email [:member/email]
                                             [:required [:member/email :member/password]]]])
                           (update :member/password hashers/derive)
                           (coast/insert)
                           (coast/rescue))]
        (if (some? errors)
          (build (merge errors request))
          (-> (coast/redirect-to ::dashboard)
              (assoc :session (select-keys (:params request) [:member/email]))))))))


(defn dashboard [request]
  [:div 
   [:h1 "You're signed in! Welcome to your fabric stash!"]
   (coast/form-for :session/delete
    [:input {:type "submit" :value "Sign out"}])])


(comment

(def debug-m (atom nil))

(add-tap #(reset! debug-m %))
;; => nil

@debug-m

)
