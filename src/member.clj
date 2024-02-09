
;;;;;; SIGN UP page (for new members)

(ns member
  (:require [coast]
            [components :refer [thead th container input label select submit link-to table]]
            [buddy.hashers :as hashers]))

;;;;;; SIGN UP form

(defn build [request]
  (container {:mw 6}


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

    [:input {:class "input-reset pointer dim db bn f6 br2 ph3 pv2 dib white bg-blue"
             :type "submit" 
             :value "SIGN UP"}]

    [:p [:br] "Already a member? " (link-to (coast/url-for :session/build) "LOGIN")])))


(defn create [request]
  (let [email (get-in request [:params :member/email])
        member (coast/find-by :member {:email email})]
    (if member ;; if existing member
      (coast/redirect-to :session/build {:error "dupe"}) ;; redirect to login dupe page. see session ns.
      (let [[_ errors] (-> (:params request)
                           (select-keys [:member/email :member/password])
                           (coast/validate [[:email [:member/email]
                                             [:required [:member/email :member/password]]]])
                           ; coast converts number strings to numbers, but buddy requires strings
                           (update :member/password str)
                           (update :member/password hashers/derive)
                           (coast/insert)
                           (coast/rescue))]
        (if (some? errors)
          (build (merge errors request))
          (-> (coast/redirect-to :fabric/index) ;; if not exisiting member and no errors, go to fabrics/index page
              (assoc :session (select-keys (:params request) [:member/email]))))))))


(defn dashboard [request]
  [:div 
   [:h1 "You're signed in! Welcome to your fabric stash!"]
   (coast/form-for :session/delete
                   [:input {:type "submit" :value "Sign out"}])])


(comment

(def debug-m (atom nil))

(add-tap #(reset! debug-m %))

@debug-m

)
