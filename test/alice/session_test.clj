(ns alice.session-test
  (:require [clojure.test :refer :all]
            [alice.session :as session]))

(deftest authenticate-test
  (is (=
       (session/authenticate {:status 200} "sacred tales")
       {:status 200 :session {:secret "sacred tales"}})))

(deftest deauthenticate-test
  (is (=
       (session/deauthenticate {:status 200 :session {:secret "sacred tales"}})
       {:status 200 :session {:secret nil}})))

(deftest authenticated?-test
  (is (session/authenticated? {:session {:secret "sacred tales"}}))
  (is (not (session/authenticated? {:session {:secret nil}}))))
