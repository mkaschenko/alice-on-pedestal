(ns alice.views
  (:require [clojure.string :as string]
            [io.pedestal.http.route :as route]))

(defn continue-link
  [page-number last-page-number]
  (if (< page-number last-page-number)
    (format "<a href=\"%s\">continue</a>"
            (route/url-for :book-page :params {:id (inc page-number)}))
    ""))

(defn book-page
  [{:keys [:title :pages :pages-count]} page-number]
  (format "<title>%s</title><h1>%s</h1><div>%s</div></br><div>%s</div>"
          title
          title
          (continue-link page-number pages-count)
          (string/join "</br>" (nth pages (dec page-number)))))
