(ns queries
  (:require [coast]
            [components]
            [fabric]))

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
; returned all fabrics 3 yards and under, only fields of yards shade color weight conten

(coast/q '["SELECT yards,shade,color,weight,content
            From fabric 
            WHERE yards<=3"])

(coast/q '[:select *
           :from fabric
           :where [color "blue"]
           :order image desc yards desc structure desc color desc])
; returns only all blue fabrics

(coast/q '[:select *
           :from fabric
           :where [color "brown"]])
; returns only all "brown" fabrics

(coast/q '[:select :distinct color
           :from fabric])
; returns all colors from db that are distinct

(mapv :color (coast/q '[:select :distinct color
                        :from fabric]))

; get all the values
;; => ["green" "black" "blue" "brown" "yellow" "grey" "purple" "red"]
