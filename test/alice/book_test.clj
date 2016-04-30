(ns alice.book-test
  (:require [clojure.test :refer :all]
            [alice.book :as book]))

(deftest paginate-test
  (is (=
       (book/paginate ["Alice" "Caterpillar" "Cheshire Cat"
                       "Jabberwock" "White Rabbit"] 2)
       [["Alice" "Caterpillar"] ["Cheshire Cat" "Jabberwock"] ["White Rabbit"]])))
