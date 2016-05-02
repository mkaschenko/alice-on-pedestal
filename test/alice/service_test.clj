(ns alice.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [alice.service :as service]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(deftest home-page-test
  (is (= (:status (response-for service :get "/")) 200)))

(deftest show-page-test
  (is (= (:status (response-for service :get "/page/2")) 200))
  (is (= (:status (response-for service :get "/page/8")) 302))
  (is (= (get (:headers (response-for service :get "/page/8")) "Location") "/"))
  (is (= (:status (response-for service :get "page/first")) 404)))

(deftest about-page-test
  (is (.contains
       (:body (response-for service :get "/about"))
       "Clojure 1.7"))
  (is (=
       (:headers (response-for service :get "/about"))
       {"Content-Type"              "text/html;charset=UTF-8"
        "Strict-Transport-Security" "max-age=31536000; includeSubdomains"
        "X-Frame-Options"           "DENY"
        "X-Content-Type-Options"    "nosniff"
        "X-XSS-Protection"          "1; mode=block"})))
