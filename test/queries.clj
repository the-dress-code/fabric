(ns queries
  (:require [coast]))













(coast/q '[:select * 
           :from fabric
           :order yards desc])
; returned all fabrics in yards descending order

(coast/q '[:select * 
           :from fabric
           :where ["yards >= 3"]])
; returned all fabrics 3 yards and greater

(coast/q '[:select * 
           :from fabric
           :where [yards 3]])
; returned all fabrics 3 yards only

(coast/q '[:select * 
           :from fabric
           :where [yards <= 3]])
; not what i want: it returned all fabrics 3 yards only, not the ones under 3

(coast/q '[:select yards shade color weight content
           :from fabric
           :where ["yards <= 3"]])
; returned all fabrics 3 yards and under

(coast/q '["SELECT yards,shade,color,weight,content
            From fabric 
            WHERE yards<=3"])
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
