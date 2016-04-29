; Copyright 2013 Relevance, Inc.
; Copyright 2014 Cognitect, Inc.

; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0)
; which can be found in the file epl-v10.html at the root of this distribution.
;
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.
;
; You must not remove this notice, or any other, from this software.

(ns io.pedestal.service-tools.dev
  (:require [io.pedestal.http :as bootstrap]
            [ns-tracker.core :as tracker]))

(defn- ns-reload [track]
  (try
    (doseq [ns-sym (track)]
      (require ns-sym :reload))
    (catch Throwable e (.printStackTrace e))))

(defn watch
    "Watches a list of directories for file changes, reloading them as necessary."
  ([] (watch ["src"]))
  ([src-paths]
   (let [track (tracker/ns-tracker src-paths)
         done (atom false)]
     (doto
         (Thread. (fn []
                    (while (not @done)
                      (ns-reload track)
                      (Thread/sleep 300))))
       (.setDaemon true)
       (.start))
     (fn [] (swap! done not)))))
