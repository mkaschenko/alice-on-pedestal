(ns alice.book-test
  (:require [clojure.test :refer :all]
            [alice.book :as book]))

(deftest paginate-test
  (is (=
       (book/paginate ["Alice" "Rabbit" "Cat" "Horse"] 2)
       ["Alice\nRabbit" "Cat\nHorse"])))

(deftest book-title-test
  (is (= book/title "Alice's Adventures in Wonderland")))

(deftest book-pages-test
  (is (.startsWith (first book/pages) "CHAPTER I. Down the Rabbit-Hole"))
  (is (.endsWith (last book/pages) "THE END"))
  (is (= (count book/pages) 111)))
