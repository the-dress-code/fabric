(ns fabric
  (:require [coast]
            [components :refer [container tc link-to table thead tbody td th tr button-to text-muted mr2 dl dd dt img submit select option input label results]]))

(defn index [request]
  (results request '[:select *
                     :from fabric
                     :order image desc yards desc structure desc color desc]))

(defn view [request]
  (let [id (-> request :params :fabric-id)
        fabric (coast/fetch :fabric id)]
    (container {:mw 8}
      (dl

        (dt "")
        (dd (img (:fabric/image fabric) (:fabric/color fabric)))
      
        (dt "yards")
        (dd (:fabric/yards fabric))

        (dt "item #")
        (dd (:fabric/item-number fabric))

        (dt "shade")
        (dd (:fabric/shade fabric))

        (dt "color")
        (dd (:fabric/color fabric))

        (dt "weight")
        (dd (:fabric/weight fabric)))

        (dt "structure")
        (dd (:fabric/structure fabric))

        (dt "content")
        (dd (:fabric/content fabric))

        (dt "width")
        (dd (:fabric/width fabric))

      (mr2
        (link-to (coast/url-for ::index) "List"))
      (mr2
        (link-to (coast/url-for ::edit {::id id}) "Edit"))
      (mr2
        (button-to (coast/action-for ::delete {::id id}) {:data-confirm "Are you sure?"} "Delete")))))


(defn errors [m]
  [:div {:class "bg-red white pa2 mb4 br1"}
   [:h2 {:class "f4 f-subheadline"} "Errors Detected"]
   [:dl
    (for [[k v] m]
      [:div {:class "mb3"}
       (dt (str k))
       (dd v)])]])


(defn build [request]
  (container {:mw 6}
    (when (some? (:errors request))
     (errors (:errors request)))

    (coast/form-for ::create

      (label {:for "fabric/image"} "image")
      (input {:type "url" :name "fabric/image"})

      (label {:for "fabric/yards"} "yards")
      (input {:type "number" :name "fabric/yards" :min "0" :max "100" :step ".25"})

      (label {:for "fabric/item-number"} "item #")
      (input {:type "text" :name "fabric/item-number"})

      (label {:for "fabric/shade"} "shade")
      (select "fabric/shade" ["" "pale" "neon" "light" "medium" "dark"])

      (label {:for "fabric/color"} "color")
      (select "fabric/color" ["" "blue" "green" "yellow" "orange" "red" "pink" "purple" "teal" "brown" "grey" "black" "white"])

      (label {:for "fabric/weight"} "weight")
      (select "fabric/weight" ["" "lightweight" "midweight" "heavyweight"])

      (label {:for "fabric/structure"} "structure")
      (select "fabric/structure" ["" "woven" "knit" "nonwoven" "skin" "felt"])

      (label {:for "fabric/content"} "content")
      (select "fabric/content" ["" "cotton" "linen" "silk" "wool" "hemp" "rayon" "nylon" "polyester"])

      (label {:for "fabric/width"} "width")
      (select "fabric/width" ["" "< 35\"" "35\"" "44\"" "45\"" "50\"" "54\"" "58\"" "59\"" "60\"" "> 60\""])

      (link-to (coast/url-for ::index) "Cancel")
      (submit "Add new fabric"))))


(defn create [request]
  (let [[_ errors] (-> (coast/validate (:params request) [[:required [:fabric/image :fabric/item-number :fabric/width :fabric/yards :fabric/structure :fabric/shade :fabric/content :fabric/color :fabric/weight]]])
                       (select-keys [:fabric/image :fabric/item-number :fabric/width :fabric/yards :fabric/structure :fabric/shade :fabric/content :fabric/color :fabric/weight])
                       (coast/insert)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (build (merge request errors)))))


(defn edit [request]
  (let [fabric (coast/fetch :fabric (-> request :params :fabric-id))]
    (container {:mw 6}
      (when (some? (:errors request))
        (errors (:errors request)))

      (coast/form-for ::change fabric

        (label {:for "fabric/image"} "image")
        (input {:type "url" :name "fabric/image" :value (:fabric/image fabric)})

        (label {:for "fabric/yards"} "yards")
        (input {:type "number" :name "fabric/yards" :min "0" :max "100" :step ".25" :value (:fabric/yards fabric)})

        (label {:for "fabric/item-number"} "item #")
        (input {:type "text" :name "fabric/item-number" :value (:fabric/item-number fabric)})

        (label {:for "fabric/shade"} "shade")
        (select "fabric/shade" {} ["pale" "neon" "light" "medium" "dark"] (:fabric/shade fabric))
   
        (label {:for "fabric/color"} "color")
        (select "fabric/color" {} ["blue" "green" "yellow" "orange" "red" "pink" "purple" "teal" "brown" "grey" "black" "white"] (:fabric/color fabric))

        (label {:for "fabric/weight"} "weight")
        (select "fabric/weight" {} ["lightweight" "midweight" "heavyweight"] (:fabric/weight fabric))

        (label {:for "fabric/structure"} "structure")
        (select "fabric/structure" {} ["woven" "knit" "nonwoven" "skin" "felt"] (:fabric/structure fabric))

        (label {:for "fabric/content"} "content")
        (select "fabric/content" {} ["cotton" "linen" "silk" "wool" "hemp" "rayon" "nylon" "polyester"] (:fabric/content fabric))

        (label {:for "fabric/width"} "width")
        (select "fabric/width" {} ["< 35\"" "35\"" "44\"" "45\"" "50\"" "54\"" "58\"" "59\"" "60\"" "> 60\""] (:fabric/width fabric))

        (link-to (coast/url-for ::index) "Cancel")
        (submit "Update fabric")))))


(defn change [request]
  (let [fabric (coast/fetch :fabric (-> request :params :fabric-id))
        [_ errors] (-> (select-keys fabric [:fabric/id])
                       (merge (:params request))
                       (coast/validate [[:required [:fabric/id :fabric/image :fabric/item-number :fabric/width :fabric/yards :fabric/structure :fabric/shade :fabric/content :fabric/color :fabric/weight]]])
                       (select-keys [:fabric/id :fabric/image :fabric/item-number :fabric/width :fabric/yards :fabric/structure :fabric/shade :fabric/content :fabric/color :fabric/weight])
                       (coast/update)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (edit (merge request errors)))))


(defn search [request]
 [:div "Hello, search page! Here I will use the results fn to build my search queries. "]
 [:a {:href (coast/url-for :fabric/index) :class "f6 link underline blue"} "back to fabrics"])


(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :fabric (-> request :params :fabric-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (-> (coast/redirect-to ::index)
          (coast/flash "Something went wrong!")))))
