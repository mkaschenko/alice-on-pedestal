(ns alice.pagination-test
  (:require [clojure.test :refer :all]
            [alice.pagination :as pagination]))

(deftest paginate-test
  (is (=
       (pagination/paginate ["Alice" "Caterpillar" "Cheshire Cat"
                             "Jabberwock" "White Rabbit"] 2)
       [["Alice" "Caterpillar"] ["Cheshire Cat" "Jabberwock"] ["White Rabbit"]])))
