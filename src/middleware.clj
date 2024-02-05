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
      (if (= :public (get public-routes (:uri request)))
        (handler request)
        (coast/unauthorized "No.")))))

;; update line 19 to the following:
#_(coast/redirect-to :member/build)

(comment

(def debug-m (atom nil))

(add-tap #(reset! debug-m %))

@debug-m

)
