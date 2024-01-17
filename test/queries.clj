(ns queries
  (:require [coast]
            [components]
            [fabric]))


(coast/q '[:select id
           :from member
           :where [:email ?email]]
        {:email "charles@barkley.com"})
;; => ({:id 21})


(coast/q '[:select id
           :from member])
;; => ({:id 1}
;;     {:id 2}
;;     {:id 3}
;;     {:id 4}
;;     {:id 5}
;;     {:id 6}
;;     {:id 7}
;;     {:id 8}
;;     {:id 9}
;;     {:id 10}
;;     {:id 11}
;;     {:id 12}
;;     {:id 13}
;;     {:id 14}
;;     {:id 15}
;;     {:id 16}
;;     {:id 17}
;;     {:id 18}
;;     {:id 19})

(map :id (coast/q '[:select id
                     :from member
                     :where [:email "wendy@804.pm"]]))
;; => (1)

(coast/q '[:select * 
           :from fabric
           :order yards desc])
; returned all fabrics in yards descending order


(coast/q '[:select * 
           :from fabric
           :where [:user-id "20"]
           :order yards desc])
;; returns all fabrics from user-id 20, by order of descending yards


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;; NEED TO SEE REQUEST

;; to be continued
(defn do-stuff [request]
  (let [user-id (-> request
                    :session
                    :member/id)])

  (coast/q '[:select * 
             :from fabric
             :where [:user-id ?user-id]
             :order yards desc]
           {:user-id user-id}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(coast/q '[:select * 
           :from fabric
           :where ["yards >= 3"]])
;; => (#:fabric{:weight "lightweight",
;;              :updated-at 1692329184,
;;              :color "green",
;;              :item-number "IL019",
;;              :width "59\"",
;;              :id 2,
;;              :structure "woven",
;;              :image
;;              "https://fabrics-store.com/images/product/FS_F_10901680260822_1000x1000.jpg",
;;              :shade "medium",
;;              :created-at 1689638412,
;;              :yards 3,
;;              :content "linen"}
;;     #:fabric{:weight "lightweight",
;;              :updated-at 1692363708,
;;              :color "black",
;;              :item-number "IL019",
;;              :width "54\"",
;;              :id 3,
;;              :structure "woven",
;;              :image
;;              "https://fabrics4fashion.com/cdn/shop/products/Black_Linen_Fabric_98186_3.jpg?v=1648398420&width=932",
;;              :shade "dark",
;;              :created-at 1689639101,
;;              :yards 5,
;;              :content "linen"}
;;     #:fabric{:weight "lightweight",
;;              :updated-at 1692496205,
;;              :color "blue",
;;              :item-number "19257021",
;;              :width "45\"",
;;              :id 9,
;;              :structure "woven",
;;              :image
;;              "https://i.etsystatic.com/6276786/r/il/4742e7/933508539/il_680x540.933508539_o4tr.jpg",
;;              :shade "dark",
;;              :created-at 1690485724,
;;              :yards 3.25,
;;              :content "cotton"}
;;     #:fabric{:weight "lightweight",
;;              :updated-at 1692328384,
;;              :color "brown",
;;              :item-number "0",
;;              :width "45\"",
;;              :id 10,
;;              :structure "woven",
;;              :image
;;              "https://raspberrycreekfabrics.com/cdn/shop/products/IMG_1668_2048x.jpg",
;;              :shade "light",
;;              :created-at 1690485869,
;;              :yards 5,
;;              :content "cotton"}
;;     #:fabric{:weight "lightweight",
;;              :updated-at 1692363521,
;;              :color "blue",
;;              :item-number "IL020",
;;              :width "58\"",
;;              :id 11,
;;              :structure "woven",
;;              :image
;;              "https://fabrics-store.com/images/product/FS_F_18511680269571_1000x1000.jpg",
;;              :shade "light",
;;              :created-at 1690486046,
;;              :yards 3,
;;              :content "linen"}
;;     #:fabric{:weight "lightweight",
;;              :updated-at nil,
;;              :color "blue",
;;              :item-number "IL090",
;;              :width "35\"",
;;              :id 33,
;;              :structure "felt",
;;              :image
;;              "https://fabrics-store.com/images/product/FS_F_21801680507936_1000x1000.jpg",
;;              :shade "light",
;;              :created-at 1692633937,
;;              :yards 4,
;;              :content "wool"})
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
