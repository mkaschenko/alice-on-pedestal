(ns alice.service
  (:require [io.pedestal.http :as bootstrap]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.http.ring-middlewares :as middlewares]

            [ring.middleware.session.cookie :as cookie]
            [ring.util.response :as ring-resp]

            [alice.pagination :refer [paginate]]
            [alice.views :as views]
            [alice.session :as session]

            [clojure.string :as string]))

(def book
  (let [pages (paginate (string/split-lines (slurp "resources/book.txt")) 35)]
    {:title       "Alice's Adventures in Wonderland"
     :pages       pages
     :pages-count (count pages)}))

(defn home-page
  [request]
  (ring-resp/response (views/book-page book 1)))

(defn book-page-page
  [request]
  (let [page-number           (read-string (get-in request [:path-params :id]))
        last-free-page-number 7]
    (if (and (> page-number last-free-page-number)
             (not (session/authenticated? request)))
      (ring-resp/redirect (route/url-for :sign-in-page))
      (ring-resp/response (views/book-page book page-number)))))

(defn sign-in-page
  [request]
  (ring-resp/response (views/sign-in-page)))

(defn authenticate
  [{{secret :secret} :params}]
  (if (string/blank? secret)
    (ring-resp/response (views/sign-in-page {:fair false}))
    (session/authenticate
     ;; TODO: use "next" parameter instead of hard-coded
     (ring-resp/redirect (route/url-for :book-page-page :params {:id 8}))
     secret)))

(defn sign-out
  [request]
  (session/deauthenticate (ring-resp/redirect (route/url-for :home-page))))

(defn secret-page
  [request]
  (if (= (session/secret request) (get-in request [:path-params :id]))
    (ring-resp/response (session/secret request))
    (ring-resp/status (ring-resp/response "No secrets!") 403)))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s" (clojure-version))))

(defroutes routes
  ;; Defines "/" and "/about" routes with their associated :get handlers.
  ;; The interceptors defined after the verb map (e.g., {:get home-page}
  ;; apply to / and its children (/about).
  [[["/" {:get [:home-page home-page]}
     ^:interceptors [(body-params/body-params) bootstrap/html-body
                     middlewares/params middlewares/keyword-params
                     (middlewares/session {:store (cookie/cookie-store)})]

     ["/page/:id" {:get [:book-page-page book-page-page]}
      ^:constraints {:id #"[0-9]+"}]

     ["/sign-in" {:get [:sign-in-page sign-in-page]}]
     ["/authenticate" {:post [:authenticate authenticate]}]
     ["/sign-out" {:get [:sign-out sign-out]}]

     ["/secrets/:id" {:get [:secret-page secret-page]}]
     ["/about" {:get about-page}]]]])

;; Consumed by alice.server/create-server
;; See bootstrap/default-interceptors for additional options you can configure
(def service {:env               :prod
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
