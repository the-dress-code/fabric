(ns session
  (:require [coast]
[components :refer [thead th container input label select submit link-to table]]
            [buddy.hashers :as hashers]))


(defn build [request]
  (container {:mw 6}
    
    (thead "LOGIN")
      [:p
       [:br]]
    
    (when (some? (:error/message request))
      [:div (:error/message request)])

    (coast/form-for ::create

     (label {:for "member/email"} "Email")
     (input {:type "text" :name "member/email" :required true})

     (label {:for "member/password"} "Password")
     (input {:type "password" :name "member/password" :required true})

     [:input {:class "input-reset pointer dim db bn f6 br2 ph3 pv2 dib white bg-blue"
             :type "submit" 
             :value "LOGIN"}]

     [:p [:br] "Need an account? " (link-to (coast/url-for :member/build) "SIGN UP")])))


(defn create [request]
  (let [email (get-in request [:params :member/email])
        member (coast/find-by :member {:email email})
        [valid? errors] (-> (:params request)
                            (select-keys [:member/email :member/password])
                            (coast/validate [[:email [:member/email]
                                              [:required [:member/email :member/password]]]]) ; these three lines could be middleware
                            (get :member/password) ; this returns the plaintext password from the params map
                            (hashers/check (:member/password member)) ; hashers/check is here
                            (coast/rescue))]
    (if (or (some? errors)
            (false? valid?)
            (nil? valid?))
      (build (merge errors request {:error/message "Invalid email or password"}))
      (-> (coast/redirect-to :member/dashboard)
          (assoc :session (select-keys (:params request) [:member/email]))))))


(defn delete [request]
  (-> (coast/redirect-to ::build)
      (assoc :session nil)))


(comment

(def debug-s (atom nil))

(add-tap #(reset! debug-s %))

@debug-s

)
