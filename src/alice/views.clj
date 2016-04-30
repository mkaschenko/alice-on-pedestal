(ns alice.views
  (:require [clojure.string :as string]
            [io.pedestal.http.route :as route]
            [alice.book :as book]))

(defn continue-link
  [page-number last-page-number]
  (if (< page-number last-page-number)
    (format "<a href=\"%s\">continue</a>"
            (route/url-for :book-page :params {:id (inc page-number)}))
    ""))

(defn book-page
  [title page page-number]
  (format "<title>%s</title><h1>%s</h1><div>%s</div></br><div>%s</div>"
          title
          title
          (continue-link page-number (count book/pages))
          (string/join "</br>" page)))
