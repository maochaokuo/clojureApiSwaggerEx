(ns comment.middleware)

(def db
  {:name ::db
   :compile (fn [route-data route-opts])})