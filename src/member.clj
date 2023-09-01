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
   [:h1 "You're signed in! Welcome to your fabric stash!"]
   (coast/form-for :session/delete
    [:input {:type "submit" :value "Sign out"}])])


(comment

(def debug-m (atom nil))

(add-tap #(reset! debug-m %))

@debug-m
; on params request on create
;; => {:__anti-forgery-token
;;     "A4kKk1KFHi7INThtqQsXCOrFP3J0Ts+OYilfc/4KKmhRJoe/PZntPVHR9gly8UsnZCke3E9/BrDSCxI9",
;;     :member/email "signuptest908@gmail.com",
;;     :member/password "hello"}

(select-keys @debug-m [:member/email :member/password])
;; => #:member{:email "signuptest908@gmail.com", :password "hello"}

(coast/validate  #:member{:email "signuptest908@gmail.com", :password "hello"} 
                 [[:email [:member/email]
                          [:required [:member/email :member/password]]]])
;; => #:member{:email "signuptest908@gmail.com", :password "hello"}

(update #:member{:email "signuptest908@gmail.com", :password "hello"} :member/password hashers/derive)
;; => #:member{:email "signuptest908@gmail.com",
;;             :password
;;             "bcrypt+sha512$9970ca4b87c21df69b48138e379f393b$12$a6406dde27b1b55443f4158684649fee5d8ff5a17110136c"}

(coast/rescue  #:member{:email "signuptest908@gmail.com",
                :password      "bcrypt+sha512$9970ca4b87c21df69b48138e379f393b$12$a6406dde27b1b55443f4158684649fee5d8ff5a17110136c"})
;; => [#:member{:email "signuptest908@gmail.com",
;;              :password
;;              "bcrypt+sha512$9970ca4b87c21df69b48138e379f393b$12$a6406dde27b1b55443f4158684649fee5d8ff5a17110136c"}
;;     nil]

(def thing {:__anti-forgery-token
             "A4kKk1KFHi7INThtqQsXCOrFP3J0Ts+OYilfc/4KKmhRJoe/PZntPVHR9gly8UsnZCke3E9/BrDSCxI9",
             :member/email "signuptest908@gmail.com",   
             :member/password "hello"})



)
