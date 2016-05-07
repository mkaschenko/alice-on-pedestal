(ns alice.views
  (:require [clojure.string :as string]
            [io.pedestal.http.route :as route]
            [hiccup.page :as hiccup]))

(defn back-link
  [page-number min-page-number]
  (if (< min-page-number page-number)
    (format "<a href=\"%s\">back</a>"
            (route/url-for :book-page-page :params {:id (dec page-number)}))
    ""))

(defn continue-link
  [page-number max-page-number]
  (if (< page-number max-page-number)
    (format "<a href=\"%s\">continue</a>"
            (route/url-for :book-page-page :params {:id (inc page-number)}))
    ""))

(defn book-page
  [{:keys [:title :pages :pages-count]} page-number]
  (format "<title>%s</title><h1>%s</h1><div>%s -- %d -- %s</div></br><div>%s</div>"
          title
          title
          (back-link page-number 1)
          page-number
          (continue-link page-number pages-count)
          (string/join "</br>" (nth pages (dec page-number)))))

(defn sign-in-page
  ([] (sign-in-page {:fair true}))
  ([{fair :fair}]
   (hiccup/html5
    [:html
     [:head
      [:title "Let's be more closer"]]
     [:body
      [:h1 (if fair
             "Tell me your secret..."
             "No, no, no! That wasn't fair")]
      [:form {:action (route/url-for :authenticate) :method "POST"}
       [:label {:for "secret"} "Secret"]
       [:input {:type "text", :name "secret"}]
       [:input {:type "submit", :name "submit", :value "Tell"}]]]])))
