(ns components
  (:require [coast]))


(defn member-email [request]
  (-> request
      :session
      :member/email))


(defn member-id [email]
  (-> (coast/q '[:select id
                 :from member
                 :where [:email ?email]]
               {:email email}) 
      first
      :id))


(defn link-to 
  ([url body]
   (link-to url "" body))
  ([url class body]
   [:a {:href url :class (str "f6 link underline blue " class)}
    body]))


(defn layout [request body]
  [:html
    [:head
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     (coast/css "bundle.css")
     (coast/js "bundle.js")]
    [:body
     [:div {:class "banner"}
      "Fabric Stash " " [ Home ] " " [ About ] " 
      (if (member-email request) 

        [:div {:class "banner"} " [ Hi,  " (member-email request) " ] " " "    
         
         (coast/form-for :session/delete
                         [:input {:class "input-reset pointer dim db bn f6 br2 ph3 pv2 dib white bg-blue"
                                  :type "submit" :value "Sign out"}])]

        [:div {:class "banner"} 
         (link-to (coast/url-for :session/build) "LOGIN")
         (link-to (coast/url-for :member/build) "SIGN UP")])]
     body]])


(defn button-to
  ([am m s]
   (let [data (select-keys m [:data-confirm])
         form (select-keys am [:action :_method :method :class])]
     (coast/form (merge {:class "dib ma0"} form)
       [:input (merge data {:class "input-reset pointer link underline bn f6 br2 ma0 pa0 dib blue bg-transparent"
                            :type "submit"
                            :value s})])))
  ([am s]
   (button-to am {} s)))


(defn container [m & body]
  (let [mw (or (:mw m) 8)]
    [:div {:class (str "pa4 w-100 center mw" mw)}
     [:div {:class "overflow-auto"}
       body]]))


(defn table [& body]
  [:table {:class "f6 w-100" :cellspacing 0}
   body])


(defn thead [& body]
  [:thead body])


(defn tbody [& body]
  [:tbody {:class "lh-copy"} body])


(defn tr [& body]
  [:tr {:class "stripe-dark"}
   body])


(defn th
  ([s]
   [:th {:class "fw6 tl pa3 bg-white"} s])
  ([]
   (th "")))


(defn td [& body]
  [:td {:class "pa3"} body])


(defn submit [value]
  [:input {:class "input-reset pointer dim ml3 db bn f6 br2 ph3 pv2 dib white bg-blue"
           :type "submit"
           :value value}])


(defn dt [s]
  [:dt {:class "f6 b mt2"} s])


(defn dd [s]
  [:dd {:class "ml0"} s])


(defn dl [& body]
  [:dl body])


(defn form-for
  ([k body]
   (form-for k {} body))
  ([k m body]
   (form-for k m {} body))
  ([k m params body]
   (coast/form-for k m (merge params {:class "pa4"})
     [:div {:class "measure"}
      body])))


(defn img [path color]
  [:img {:src (str path) 
         :alt (str color)
         :style "max-width:100px;height:auto;"}])


(defn label [m s]
  [:label (merge {:for s :class "f6 b db mb2"} m) s])


(defn input [m]
  [:input (merge {:class "input-reset ba b--black-20 pa2 mb2 db w-100 outline-0"} m)])


(defn option [o s]
  (if (= o s)
    [:option {:value o :selected "selected"} o]
    [:option {:value o} o]))


(defn map-option [o s]
  (map (fn [x] (option x s)) o))


(defn select 
  ([l o] 
    (select l {} o))
  ([l m o]
    (select l m o ""))
  ([l m o s]
   (apply (partial conj 
           [:select (merge {:class "input-reset ba b--black-20 pa2 mb2 db w-100 outline-0"} {:name l :id l} m)])
          (map-option o s))))


(defn text-muted [s]
  [:div {:class "f6 tc gray"}
   s])


(defn el [k m]
  (fn [& body]
    [k m body]))


(->> [:mr1 :mr2 :mr3 :mr4 :mr5]
     (mapv name)
     (mapv #(coast/intern-var % (el :span {:class %}))))


(defn tc [& body]
  [:div {:class "tc"}
   body])

(defn results 
  ([request query]
   (results request query {}))
  ([request query q-args]
   (let [rows (coast/q query q-args)]

     (container {:mw 8}
                (when (not (empty? rows))
                  `(~(link-to (coast/url-for :fabric/build) "left-link" "Add new fabric")
                    ~(link-to (coast/url-for :fabric/search) "right-link" "Search for fabric")))
                
                (when (empty? rows)
                  (tc
                   (link-to (coast/url-for :fabric/build) "Add new fabric")))

                (when (not (empty? rows))
                  (table
                   (thead
                    (tr
                     (th "")
                     (th "yards")
                     (th "shade")  
                     (th "color")
                     (th "weight")
                     (th "structure")
                     (th "content")
                     (th "width")))
                   (tbody
                    (for [row rows]
                      (tr
                       (td (img (:fabric/image row) (:fabric/color row)))
                       (td (:fabric/yards row))              
                       (td (:fabric/shade row))
                       (td (:fabric/color row))
                       (td (:fabric/weight row))
                       (td (:fabric/structure row))
                       (td (:fabric/content row))
                       (td (:fabric/width row))
                       (td
                        (link-to (coast/url-for :fabric/view row) "View"))
                       (td
                        (link-to (coast/url-for :fabric/edit row) "Edit"))
                       (td
                        (button-to (coast/action-for :fabric/delete row) {:data-confirm "Are you sure?"} "Delete")))))))))))


(defn get-column [kw column table]
  (apply vector (cons "" (mapv kw (coast/q '[:select :distinct ?column
                                            :from ?table]
                                          {:column column
                                           :table table})))))


(defn yards-min [x]
  [(str "yards >= " x)])


(defn where-filters [params]
  (let [no-token (dissoc params :__anti-forgery-token)]
    (reduce (fn [acc [k v]] 
              (if (nil? v)
                acc
                (if (= k :fabric/yards)
                  [(yards-min v)]
                  (conj acc [(-> k
                                 name
                                 symbol)
                             (->> k 
                                  name
                                  (str "?")
                                  symbol)])))) 
            []
            no-token)))
