(ns app.ui.root
  (:require
    [fulcro.client.mutations :as m]
    [fulcro.client.data-fetch :as df]
    #?(:cljs [fulcro.client.dom :as dom] :clj [fulcro.client.dom-server :as dom])
    [app.api.mutations :as api]
    [fulcro-css.css :as css]
    [fulcro.client.primitives :as prim :refer [defsc]]
    [fulcro-css.css-injection :as injection]
    [garden.stylesheet :refer [at-media]]
    [garden.units :refer [px vw vh]]
    [fulcro.i18n :as i18n :refer [tr trf]]))

;; The main UI of your application
(defsc Header [this
               {:keys [title]} computed
               {:keys [header] :as css-classes}]
  {:query [:title]
   :css [[:.header {:text-align "center"}]]
   }
  (dom/div #js {:className header}
    (dom/h1 nil "Welcome")))

(def ui-header (prim/factory Header {:keyfn :label}))

;; Person function
(defsc Person [this
               {:keys [db/id person/name person/age]}
               {:keys [onDelete] :as computed}
               {:keys [item btn] :as css-classes}]
  {:query [:db/id :person/name :person/age :item :btn]
   :css [[:.item {:font-weight "600" :color "black" :list-style "none" }
          (at-media {:screen true :min-width (px 1024)} [:&:hover {:color "white"}])
          (at-media {:screen true :min-width (px 768) :max-width (px 1023)} [:&:hover {:color "white"}])
          ]
         [:.btn {:color "white" :border-radius 0 :background-color "black" :outline (px 1) :margin 0} [:&:hover {:background-color "white" :color "black"}]
          ]]
   
   :ident [:person/by-id :db/id]
   :initial-state (fn [{:keys [id name age item]}] {:db/id id :person/name name :person/age age :item item})}
  (dom/li  #js {:className item}
    (dom/h5 (str name " (age: " age ")  ")
            (dom/button {:className btn :onClick #(onDelete id)} "x"))))

(def ui-person (prim/factory Person {:keyfn :person/name}))

;; Person List
(defsc PersonList [this
                   {:keys [db/id person-list/label person-list/people]} computed
                   {:keys [labels items-wrapper container] :as css-classes}]
  {:query [:db/id :person-list/label {:person-list/people (prim/get-query Person)} :labels :items-wrapper :container]
   :css [[:.items-wrapper {:border "2px solid black" :margin (px 10)}
          (at-media
            {:screen true :min-width (px 1024)}
            [:& {:border "2px solid black" :background-color "red" :margin (px 20)}])
          (at-media
            {:screen true :min-width (px 768) :max-width (px 1023)}
            [:& {:border "2px solid black" :background-color "blue" :margin (px 10)}]
            [:&:hover {:color "white"}])
          ]
         [:.labels
          {:padding-top (px 20) :margin-left (px 30)}
          ]
         [:.container
          {:width (vw 70) :margin "0 auto"}
          ]]
   :ident [:person-list/by-id :db/id]
   :initial-state
          (fn [{:keys [id label]}]
            {:db/id              id
             :person-list/label  label
             :person-list/people (if (= label "Friends")
                                   [(prim/get-initial-state Person {:id 1 :name "Sally" :age 32})
                                    (prim/get-initial-state Person {:id 2 :name "Joe" :age 22})]
                                   [(prim/get-initial-state Person {:id 3 :name "Fred" :age 11})
                                    (prim/get-initial-state Person {:id 4 :name "Bobby" :age 55})])})}
  (let [delete-person (fn [person-id] (prim/transact! this `[(api/delete-person {:list-id ~id :person-id ~person-id})]))]
    
    (dom/div #js {:className container}
    (dom/div #js {:className items-wrapper}
      (dom/h4 {:className labels} label ":")
      (dom/ul
        (map (fn [p] (ui-person (prim/computed p {:onDelete delete-person}))) people))))))

(def ui-person-list (prim/factory PersonList))


;; Root Application 
(defsc Root [this {:keys [ui/react-key friends enemies]}]
  {:query         [{:header (prim/get-query Header)} :ui/react-key {:friends (prim/get-query PersonList)}
                   {:enemies (prim/get-query PersonList)}]
   :initial-state (fn [params] {:friends (prim/get-initial-state PersonList {:id :friends :label "Friends"})
                                :enemies (prim/get-initial-state PersonList {:id :enemies :label "Enemies"})}
                    )}
  (dom/div
    (injection/style-element {:component this})
    (ui-header)
    (ui-person-list friends)
    (ui-person-list enemies)))

