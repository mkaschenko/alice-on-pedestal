(ns alice.views
  (:require [clojure.string :as string]
            [io.pedestal.http.route :as route]
            (hiccup core page)))

(defn back-link
  [page-number min-page-number]
  (if (< min-page-number page-number)
    (format "<a href=\"%s\">back</a>"
            (route/url-for :book-page-page :params {:id (dec page-number)}))
    ""))

(defn continue-link
  [page-number max-page-number]
  (if (< page-number max-page-number)
    (hiccup.core/html
     [:a
      {:href (route/url-for :book-page-page :params {:id (inc page-number)})}
      "continue"])
    ""))

(defn secret-page-link
  [secret]
  (let [url (route/url-for :secret-page :params {:id secret})]
    (hiccup.core/html
     [:a {:href url} url])))

(defn book-page
  [{:keys [:title :pages :pages-count]} page-number]
  (format "<title>%s</title>
           <h1>%s</h1>
           <div>%s -- %d -- %s</div>
           </br>
           <div>%s</div>"
          title
          title
          (back-link page-number 1)
          page-number
          (continue-link page-number pages-count)
          (string/join "</br>" (nth pages (dec page-number)))))

(defn sign-in-page
  ([] (sign-in-page {:fair true}))
  ([{fair :fair}]
   (hiccup.page/html5
    [:html
     [:head
      [:title "What is your secret..."]]
     [:body
      [:h1 (if fair
             "What is your secret..."
             "No, no, no! That wasn't fair.")]
      [:form {:action (route/url-for :authenticate) :method "POST"}
       [:input {:type "text", :name "secret", :placeholder "Your secret"}]
       [:input {:type "submit", :name "submit", :value "Tell"}]]
      [:p
       [:i "Visit your secret page at " (secret-page-link "your-secret")]]]])))
