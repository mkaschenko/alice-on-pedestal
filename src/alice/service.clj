(ns alice.service
  (:require [io.pedestal.http :as bootstrap]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [ring.util.response :as ring-resp]
            ;; TODO: remove
            [alice.book :as book]
            [alice.views :as views]
            [clojure.string :as string]))

(def book
  (let [pages (book/paginate (string/split-lines (slurp "resources/book.txt")) 35)]
    {:title "Alice's Adventures in Wonderland"
     :pages pages
     :pages-count (count pages)}))

(defn home-page
  [request]
  (ring-resp/response (views/book-page book 1)))

(defn book-page
  [{{page-number :id} :path-params}]
  (ring-resp/response (views/book-page book (read-string page-number))))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s" (clojure-version))))

(defroutes routes
  ;; Defines "/" and "/about" routes with their associated :get handlers.
  ;; The interceptors defined after the verb map (e.g., {:get home-page}
  ;; apply to / and its children (/about).
  [[["/" {:get home-page}
     ^:interceptors [(body-params/body-params) bootstrap/html-body]

     ["/page/:id" {:get [:book-page book-page]}
      ^:constraints {:id #"[0-9]+"}]

     ["/about" {:get about-page}]]]])

;; Consumed by alice.server/create-server
;; See bootstrap/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::bootstrap/interceptors []
              ::bootstrap/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::bootstrap/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::bootstrap/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ::bootstrap/type :jetty
              ;;::bootstrap/host "localhost"
              ::bootstrap/port 8080})
