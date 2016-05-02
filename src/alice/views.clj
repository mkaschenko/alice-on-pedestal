(ns alice.views
  (:require [clojure.string :as string]
            [io.pedestal.http.route :as route]))

(defn back-link
  [page-number min-page-number]
  (if (< min-page-number page-number)
    (format "<a href=\"%s\">back</a>"
            (route/url-for :show-page :params {:id (dec page-number)}))
    ""))

(defn continue-link
  [page-number max-page-number]
  (if (< page-number max-page-number)
    (format "<a href=\"%s\">continue</a>"
            (route/url-for :show-page :params {:id (inc page-number)}))
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
