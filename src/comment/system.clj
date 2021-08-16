(ns comment.system
  (:require [integrant.core :as ig]))

(def system-config
  {::a {:b (ig/ref ::b)}
   ::b {:c (ig/ref ::c)}
   ::c nil})

(defmethod ig/init-key ::a [a {:keys [b]}]
  (println "Initialize" a)
  a)

(defmethod ig/init-key ::b [b {:keys [c]}]
  (println "Initialize" b)
  b)

(defmethod ig/init-key ::c [c _]
  (println "Initialized" c)
  c)

(defmethod ig/halt-key! ::a [_ a]
  (println "Halt" a))

(defmethod ig/halt-key! ::b [_ b]
  (println "Halt" b))

(defmethod ig/halt-key! ::c [_ c]
  (println "Halt" c))

(comment
  (def system (ig/init system-config))
  )