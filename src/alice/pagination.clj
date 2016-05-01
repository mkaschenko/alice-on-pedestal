(ns alice.pagination)

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
