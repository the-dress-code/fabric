(ns fabric
  (:require [coast]
            [components :refer [container tc link-to table thead tbody td th tr button-to text-muted mr2 dl dd dt submit input label]]))


(defn index [request]
  (let [rows (coast/q '[:select *
                        :from fabric
                        :order id
                        :limit 10])]
    (container {:mw 8}
     (when (not (empty? rows))
      (link-to (coast/url-for ::build) "New fabric"))

     (when (empty? rows)
      (tc
        (link-to (coast/url-for ::build) "New fabric")))

     (when (not (empty? rows))
       (table
        (thead
          (tr
            (th "width")
            (th "yards")
            (th "structure")
            (th "id")
            (th "color-intensity")
            (th "updated-at")
            (th "created-at")
            (th "content")
            (th "color")
            (th "weight")))
        (tbody
          (for [row rows]
            (tr
              (td (:fabric/width row))
              (td (:fabric/yards row))
              (td (:fabric/structure row))
              (td (:fabric/id row))
              (td (:fabric/color-intensity row))
              (td (:fabric/updated-at row))
              (td (:fabric/created-at row))
              (td (:fabric/content row))
              (td (:fabric/color row))
              (td (:fabric/weight row))
              (td
                (link-to (coast/url-for ::view row) "View"))
              (td
                (link-to (coast/url-for ::edit row) "Edit"))
              (td
                (button-to (coast/action-for ::delete row) {:data-confirm "Are you sure?"} "Delete"))))))))))


(defn view [request]
  (let [id (-> request :params :fabric-id)
        fabric (coast/fetch :fabric id)]
    (container {:mw 8}
      (dl
        (dt "width")
        (dd (:fabric/width fabric))

        (dt "yards")
        (dd (:fabric/yards fabric))

        (dt "structure")
        (dd (:fabric/structure fabric))

        (dt "color-intensity")
        (dd (:fabric/color-intensity fabric))

        (dt "content")
        (dd (:fabric/content fabric))

        (dt "color")
        (dd (:fabric/color fabric))

        (dt "weight")
        (dd (:fabric/weight fabric)))
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
      (label {:for "fabric/width"} "width")
      (input {:type "text" :name "fabric/width" :value (-> request :params :fabric/width)})

      (label {:for "fabric/yards"} "yards")
      (input {:type "text" :name "fabric/yards" :value (-> request :params :fabric/yards)})

      (label {:for "fabric/structure"} "structure")
      (input {:type "text" :name "fabric/structure" :value (-> request :params :fabric/structure)})

      (label {:for "fabric/color-intensity"} "color-intensity")
      (input {:type "text" :name "fabric/color-intensity" :value (-> request :params :fabric/color-intensity)})

      (label {:for "fabric/content"} "content")
      (input {:type "text" :name "fabric/content" :value (-> request :params :fabric/content)})

      (label {:for "fabric/color"} "color")
      (input {:type "text" :name "fabric/color" :value (-> request :params :fabric/color)})

      (label {:for "fabric/weight"} "weight")
      (input {:type "text" :name "fabric/weight" :value (-> request :params :fabric/weight)})

      (link-to (coast/url-for ::index) "Cancel")
      (submit "New fabric"))))


(defn create [request]
  (let [[_ errors] (-> (coast/validate (:params request) [[:required [:fabric/width :fabric/yards :fabric/structure :fabric/color-intensity :fabric/content :fabric/color :fabric/weight]]])
                       (select-keys [:fabric/width :fabric/yards :fabric/structure :fabric/color-intensity :fabric/content :fabric/color :fabric/weight])
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
        (label {:for "fabric/width"} "width")
        (input {:type "text" :name "fabric/width" :value (:fabric/width fabric)})

        (label {:for "fabric/yards"} "yards")
        (input {:type "text" :name "fabric/yards" :value (:fabric/yards fabric)})

        (label {:for "fabric/structure"} "structure")
        (input {:type "text" :name "fabric/structure" :value (:fabric/structure fabric)})

        (label {:for "fabric/color-intensity"} "color-intensity")
        (input {:type "text" :name "fabric/color-intensity" :value (:fabric/color-intensity fabric)})

        (label {:for "fabric/content"} "content")
        (input {:type "text" :name "fabric/content" :value (:fabric/content fabric)})

        (label {:for "fabric/color"} "color")
        (input {:type "text" :name "fabric/color" :value (:fabric/color fabric)})

        (label {:for "fabric/weight"} "weight")
        (input {:type "text" :name "fabric/weight" :value (:fabric/weight fabric)})

        (link-to (coast/url-for ::index) "Cancel")
        (submit "Update fabric")))))


(defn change [request]
  (let [fabric (coast/fetch :fabric (-> request :params :fabric-id))
        [_ errors] (-> (select-keys fabric [:fabric/id])
                       (merge (:params request))
                       (coast/validate [[:required [:fabric/id :fabric/width :fabric/yards :fabric/structure :fabric/color-intensity :fabric/content :fabric/color :fabric/weight]]])
                       (select-keys [:fabric/id :fabric/width :fabric/yards :fabric/structure :fabric/color-intensity :fabric/content :fabric/color :fabric/weight])
                       (coast/update)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (edit (merge request errors)))))


(defn delete [request]
  (let [[_ errors] (-> (coast/fetch :fabric (-> request :params :fabric-id))
                       (coast/delete)
                       (coast/rescue))]
    (if (nil? errors)
      (coast/redirect-to ::index)
      (-> (coast/redirect-to ::index)
          (coast/flash "Something went wrong!")))))
