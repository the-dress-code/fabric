(ns queries
  (:require [coast]))

(coast/q '[:select * 
           :from fabric
           :order yards desc])

(coast/q '[:select * 
           :from fabric
           :where ["yards >= 3"]])

(coast/q '[:select * 
           :from fabric
           :where [yards 3]])
;; => (#:fabric{:updated-at 1689958553,
;;              :color "green",
;;              :content "linen",
;;              :width "51\"",
;;              :weight "mid-weight",
;;              :structure "woven",
;;              :id 2,
;;              :created-at 1689638412,
;;              :yards 3,
;;              :shade "medium"}
;;     #:fabric{:updated-at 1689719833,
;;              :color "brown white",
;;              :content "linen",
;;              :width "54\"",
;;              :weight "mid-weight",
;;              :structure "woven",
;;              :id 6,
;;              :created-at 1689645331,
;;              :yards 3,
;;              :shade "light"})

(coast/q '[:select yards shade color weight content
           :from fabric
           :where ["yards <= 3"]])
;; => ({:yards 3,
;;      :shade "medium",
;;      :color "green",
;;      :weight "mid-weight",
;;      :content "linen"}
;;     {:yards 1,
;;      :shade "light",
;;      :color "blue",
;;      :weight "light-weight",
;;      :content "cotton"}
;;     {:yards 2,
;;      :shade "dark",
;;      :color "black",
;;      :weight "mid-weight",
;;      :content "cotton lycra"}
;;     {:yards 3,
;;      :shade "light",
;;      :color "brown white",
;;      :weight "mid-weight",
;;      :content "linen"}
;;     {:yards 1,
;;      :shade "light",
;;      :color "multi",
;;      :weight "light-weight",
;;      :content "cotton"}
;;     {:yards 1,
;;      :shade "med",
;;      :color "green",
;;      :weight "mid",
;;      :content "linen"})

(fetet (1))


(coast/q '["SELECT yards,shade,color,weight,content From fabric WHERE yards<=3"])
;; => ({:yards 3,
;;      :shade "medium",
;;      :color "green",
;;      :weight "mid-weight",
;;      :content "linen"}
;;     {:yards 1,
;;      :shade "light",
;;      :color "blue",
;;      :weight "light-weight",
;;      :content "cotton"}
;;     {:yards 2,
;;      :shade "dark",
;;      :color "black",
;;      :weight "mid-weight",
;;      :content "cotton lycra"}
;;     {:yards 3,
;;      :shade "light",
;;      :color "brown white",
;;      :weight "mid-weight",
;;      :content "linen"}
;;     {:yards 1,
;;      :shade "light",
;;      :color "multi",
;;      :weight "light-weight",
;;      :content "cotton"}
;;     {:yards 1,
;;      :shade "med",
;;      :color "green",
;;      :weight "mid",
;;      :content "linen"})
