(ns alice.book
  (:require [clojure.string :as string]))

(defn paginate
  "Split lines into pages"
  ([lines lines-per-page]
   (paginate lines lines-per-page []))
  ([lines lines-per-page pages]
   (if (empty? lines)
     pages
     (paginate (drop lines-per-page lines)
               lines-per-page
               (conj pages (take lines-per-page lines))))))

(def title "Alice's Adventures in Wonderland")

(def pages
  (paginate
   (string/split-lines (slurp "resources/book.txt"))
   30))

(defn html-page
  "Construct page HTML view"
  [title page]
  (format "<title>%s</title><h1>%s</h1></br></br><div>%s</div>"
          title title (string/join "</br>" page)))
