(ns session
  (:require [coast]
            [buddy.hashers :as hashers]))


(defn build [request]
  [:div
    (when (some? (:error/message request))
      [:div (:error/message request)])
    (coast/form-for ::create
      [:input {:type "text" :name "member/email"}]
      [:input {:type "password" :name "member/password"}]
      [:input {:type "submit" :value "Submit"}])])


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
