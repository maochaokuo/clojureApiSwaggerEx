(ns comment.handler 
  (:require
   [reitit.core :as r]))

(def routes
  [["/ping" {:name ::ping}]
   ["/pong" ::pong]
   ["/api" {:a :1}
    ["/users" ::users]
    ["/posts" ::posts]]])

(def router
  (r/router routes))

(r/match-by-path router "/ping")

(comment
  (r/routes router)
  )