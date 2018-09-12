(ns app.ui.root
  (:require
   [fulcro.client.mutations :as m]
   [fulcro.client.data-fetch :as df]
   #?(:cljs [fulcro.client.dom :as dom] :clj [fulcro.client.dom-server :as dom])
   [app.api.mutations :as api]
   [fulcro-css.css :as css]
   [fulcro.client.routing :as r :refer [defrouter]]
   [fulcro.client :as fc]
   [fulcro.client.primitives :as prim :refer [defsc]]
   [fulcro-css.css-injection :as injection]
   [garden.stylesheet :refer [at-media]]
   [fulcro.ui.elements :as ele]
   [garden.units :refer [px vw vh]]
   [fulcro.i18n :as i18n :refer [tr trf]]))

;; The main UI of your application

;; Header Index
(defsc Header [_ _]
  (dom/article {:className "athelas"}
    (dom/div {:className "vh-100 dt w-100 tc bg-dark-gray white cover" :style {:background "url(http://mrmrs.github.io/photos/u/009.jpg) no-repeat center;"}}
      (dom/div {:className "dtc v-mid"}
        (dom/header {:className "white-70"}
          (dom/h2 {:className "f6 fw1 ttu tracked mb2 lh-title"} "Issue Music")
          (dom/h1 {:className "f6 fw1 lh-title"} "Summer XCVV"))
        (dom/h1 {:className "f1 f-headline-l fw1 i white-60"} "The Chronicles")
        (dom/blockquote {:className "ph0 mh0 measure f4 lh-copy center"}
          (dom/p {:className "fw1 white-70"} "It's the space you put between the notes that make the music.")
          (dom/cite {:className "f6 ttu tracked fs-normal"} "Massimo Vignelli"))))
    )
  )

(def index-header (prim/factory Header))

;; Index
(defsc Index [this {:keys [db/id router/page]}]
  {:query         [:db/id :router/page]
   :ident         (fn [] [page id])                         ; IMPORTANT! Look up both things, don't use the shorthand for idents on screens!
   :initial-state {:db/id 1 :router/page :PAGE/index}}
  (dom/div
      (index-header)))

(defsc ArticleAbout [_ _]
  (dom/article {:className "cf ph3 ph5-ns pv5"}
    (dom/header {:className "fn fl-ns w-50-ns pr4-ns"}
      (dom/h1 {:className "mb3 mt0 lh-title"} "Clay in a Potter's Hand")
      (dom/time {:className "f6 ttu tracked gray"} "Jan Tschichold"))
    (dom/div {:className "fn fl-ns w-50-ns"}
      (dom/p {:className "lh-copy measure mt4 mt0-ns"}
        "PERFECT typography is more a science than an art. Mastery of the trade is
      indispensable, but it isn't everything. Unerring taste, the hallmark of
      perfection, rests also upon a clear understanding of the laws of harmonious
      design. As a rule, impeccable taste springs partly from inborn sensitivity:
      from feeling. But feelings remain rather unproductive unless they can inspire a
      secure judgment. Feelings have to mature into knowledge about the consequences
      of formal decisions. For this reason, there are no born masters of typography,
      but self- education may lead in time to mastery.")
      (dom/p {:className "lh-copy meassure"}
        "It is wrong to say that there is no arguing about taste when it is good taste
      that is in question. We are not born with good taste, nor do we come into this
      world equipped with a real understanding of art. Merely to recognize who or
      what is represented in a picture has little to do with a real under- standing
      of art. Neither has an uninformed opinion about the proportions of Roman
      letters. In any case, arguing is senseless. He who wants to convince has to
      do a better job than others."))))

(def article-about (prim/factory ArticleAbout))
;; About
(defsc About [this {:keys [db/id router/page]}]
  {:query         [:db/id :router/page]
   :ident         (fn [] [page id])
   :initial-state {:db/id 1 :router/page :PAGE/about}}
  (dom/div
      (article-about)))

;; Store
(defsc Store [this {:keys [db/id router/page]}]
  {:query [:db/id :router/page]
   :ident (fn [] [page id])
   :initial-state {:db/id 1 :router/page :PAGE/store}}
  (dom/div
      (dom/section {:className "flex-ns vh-100 items-center"}
        (dom/div {:className "mw6 ph5"}
          (dom/img {:src "http://tachyons.io/img/iPhone7Vertical.jpg"}))
        (dom/div {:className "tc tl-ns ph3"}
          (dom/h1 {:className "f3 f1-l fw2 mb3 mt4 mt0-ns"} "Connect in a whole new way.")
          (dom/h2 {:className "f5 f3-l fw1 mb4 mb5-l lh-title"} "Rated the #1 app for communicating with customers.")))))

;; Sign Up
(defsc Signup [this {:keys [db/id router/page]}]
  {:query [:db/id :router/page]
   :ident (fn [] [page id])
   :initial-state {:db/id 1 :router/page :PAGE/signup}}
  (dom/main {:className "pa4 black-80"}
    (dom/form {:className "measure center"}
      (dom/fieldset {:className "ba b--transparent ph0 mh0" :id "sign_up"}
        (dom/legend {:className "f4 fw6 ph0 mh0"} "Sign Up")
        (dom/div {:className "mt3"}
          (dom/label {:className "db fw6 lh-copy f6"} "Email")
          (dom/input {:className "pa2 input-reset ba bg-transparent hover-bg-black hover-white w-100" :type "email"
                      :name "email-address" :id "email"}))
        (dom/div {:className "mt3"}
          (dom/label {:className "db fw6 lh-copy f6"} "Password")
          (dom/input {:className "b pa2 input-reset ba bg-transparent hover-bg-black hover-white w-100" :type "password"
                      :name "password" :id "password"})))
      (dom/div {:className "lh-copy mt3"}
        (dom/input {:className "b ph3 pv2 input-reset ba b--black bg-transparent grow pointer f6 dib" :type "submit"
                    :value "Sign Up"}))
      (dom/div {:className "lh-copy mt3"}
        (dom/a {:className "link dim black f6 f5-ns dib pointer"
                :onClick #(prim/transact! this `[(r/set-route
                                                  {:router :root/router
                                                   :target [:PAGE/login 1]})])
                :title "Login"} "Back to Login")))))


;; Login
(defsc Login [this {:keys [db/id router/page]}]
  {:query [:db/id :router/page]
   :ident (fn [] [page id])
   :initial-state {:db/id 1 :router/page :PAGE/login}}
  (dom/main {:className "pa4 black-80"}
    (dom/form {:className "measure center"}
      (dom/fieldset {:className "ba b--transparent ph0 mh0" :id "sign_in"}
        (dom/legend {:className "f4 fw6 ph0 mh0"} "Sign In")
        (dom/div {:className "mt3"}
          (dom/label {:className "db fw6 lh-copy f6"} "Email")
          (dom/input {:className "pa2 input-reset ba bg-transparent hover-bg-black hover-white w-100" :type "email"
                      :name "email-address" :id "email-address"}))
        (dom/div {:className "mt3"}
          (dom/label {:className "db fw6 lh-copy f6"} "Password")
          (dom/input {:className "b pa2 input-reset ba bg-transparent hover-bg-black hover-white w-100" :type "password"
                      :name "password" :id "password"})))
      (dom/div
          (dom/input {:className "b ph3 pv2 input-reset ba b--black bg-transparent grow pointer f6 dib" :type "submit"
                      :value "Sign In"}))
      (dom/div {:className "lh-copy mt3"}
        (dom/a {:className "link dim black f6 f5-ns dib pointer"
                :onClick #(prim/transact! this `[(r/set-route
                                                  {:router :root/router
                                                   :target [:PAGE/signup 1]})])
                :title "Signup"} "Signup")
        (dom/a {:className "f6 link dim black db pointer"
                :onClick #(prim/transact! this `[(r/set-route
                                                  {:router :root/router
                                                   :target [:PAGE/forgot 1]
                                                   })])} "Forgot Your Password?")))))

(defsc Forgot [this {:keys [db/id router/page]}]
  {:query [:db/id :router/page]
   :ident (fn [] [page id])
   :initial-state {:db/id 1 :router/page :PAGE/forgot}}
  (dom/main {:className "pa4 black-80"}
    (dom/form {:className "measure center"}
      (dom/fieldset {:className "ba4 b--transparent ph0 mh0" :id "forgot_password"}
        (dom/legend {:className "f4 fw6 ph0 mh0"} "Forgot Your Password?")
        (dom/div {:className "mt3"}
          (dom/label {:className "db fw6 lh-copy f6"} "Email")
          (dom/input {:className "pa2 input-reset ba bg-transparent hover-bg-black hover-white w-100" :type "email"
                      :name "email-address" :id "email-address"})
          (dom/div
              (dom/input {:className "b ph3 pv2 input-reset ba b--black bg-transparent grow pointer f6 dib" :type "submit"
                          :value "Reset Password"}))
          (dom/div {:className "lh-copy mt3"}
            (dom/a {:className "link dim black f6 f5-ns dib pointer"
                    :onClick #(prim/transact! this `[(r/set-route
                                                      {:router :root/router
                                                       :target [:PAGE/login 1]
                                                       })])} "Back to login"))

          ))
      ))
  )
;; Router
(defrouter RootRouter :root/router
  [:router/page :db/id]
  :PAGE/index Index
  :PAGE/about About
  :PAGE/store Store
  :PAGE/login Login
  :PAGE/signup Signup
  :PAGE/forgot Forgot)
(def ui-root-router (prim/factory RootRouter))

;; Nav
(defsc Nav [this {:keys [title router]} computed {:keys [navbar] :as css-classes}]
  {:query [:title :navbar {:router (prim/get-query RootRouter)}]
   :css [[:.navbar {:color "black"}]]
   :intial-state (fn [p] {:reouter (prim/get-initial-state RootRouter {})})
   }

  (dom/nav {:className "dt w-100 border-box ph3 pv3 pv4-ns ph4-m ph5-l athelas"}
    (dom/a {:className "dtc v-mid mid-gray b link dim w-25" :href "#" :title "Home"} "XCVV")
    (dom/div {:className "dtc v-mid w-75 tr"}
      (dom/a {:className "link dim black f6 f5-ns dib mr3 pointer"
              :onClick #(prim/transact! this `[(r/set-route
                                                {:router :root/router
                                                 :target [:PAGE/index 1]})])
              :title "Home"} "Home")
      (dom/a {:className "link dim black f6 f5-ns dib mr3 pointer"
              :onClick #(prim/transact! this `[(r/set-route
                                                {:router :root/router
                                                 :target [:PAGE/about 1]})])
              :title "About"} "About")
      (dom/a {:className "link dim black f6 f5-ns dib mr3 pointer"
              :onClick #(prim/transact! this `[(r/set-route
                                                {:router :root/router
                                                 :target [:PAGE/store 1]})])
              :title "Contact"} "Contact")
      (dom/a {:className "link dim black f6 f5-ns dib pointer"
              :onClick #(prim/transact! this `[(r/set-route
                                                {:router :root/router
                                                 :target [:PAGE/login 1]})])
              :title "Login"} "Login")
      )))

(def ui-nav (prim/factory Nav))

(defsc Footer [_ _]
  (dom/footer {:className "pv4 ph3 ph5-m ph6-l mid-gray"}
    (dom/small {:className "f6 db tc"} "© 2018 " (dom/b {:className "ttu"} "XCVV Company Inc")"., All Rights Reserved ")
    (dom/div {:className "tc mt3"}
      (dom/a {:href "#" :className "f6 dib ph2 link mid-gray dim" :title "Language"} "Language")
      (dom/a {:href "#" :className "f6 dib ph2 link mid-gray dim" :title "Terms of Use"} "Terms of Use")
      (dom/a {:href "#" :className "f6 dib ph2 link mid-gray dim" :title "Privacy"} "Privacy"))))

(def ui-footer (prim/factory Footer))

;; Root of Application
(defsc Root [this {:keys [router]}]
  {:initial-state (fn [p] {:router (prim/get-initial-state RootRouter {})})
   :query         [{:router (prim/get-query RootRouter)}]} 
  (dom/div
      (ui-nav)
    (ui-root-router router)
    (ui-footer)))
