(ns middleware
  (:require [coast]))


(defn auth [handler]
  (fn [request]
    (if (some? (get-in request [:session :member/email]))
      (handler request)
      (coast/unauthorized "HAL9000 says: I'm sorry Wendy, I can't let you do that"))))

(defn gate-keeper [handler]
  (fn [request]
    (let [public-routes {"/sign-up" :public 
                         "/members" :public
                         "/login" :public
                         "/sessions" :public}]
      (if (components/member-email request) ;; if there is a member email in the request,
        (handler request) ;; let 'em do what they want
        (if (= :public (get public-routes (:uri request))) ;; otherwise, check if route is public
          (handler request) ;; if so, let 'em do what they want
          (coast/redirect-to :session/build)))))) ;; otherwise, re-direct to the login page


(comment

(def debug-m (atom nil))

(add-tap #(reset! debug-m %))

@debug-m

)
