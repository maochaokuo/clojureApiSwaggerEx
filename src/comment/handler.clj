(ns comment.handler 
  (:require
   [reitit.core :as r]))

(def routes
  [["/ping" {:name ::ping}]
   ["/pong" ::pong]
   ["/api" {:a :1}
    ["/users" ::users]
    ["/users/:id" ::user-id]
    ["/posts" ::posts]]])

(def router
  (r/router routes))

(r/match-by-path router "/ping")
(r/match-by-path router "/api/users/1")

(comment
  (r/routes router)
  )