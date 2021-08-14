(ns comment.handler 
  (:require
   [reitit.core :as r]
   [reitit.ring :as ring]))

(def routes
  [["/ping" {:get (fn [req] {:status 200 :body "ok"})}] ; fn replace lamda char
   ["/api"
    ["/users" ::users]
    ["/users/:id" ::user-id]
    ["/posts" ::posts]]])

(def router
  (ring/router routes))

(r/routes router)

(r/match-by-path router "/ping")
(r/match-by-path router "/api/users/333")

(r/match-by-name router ::ping)

(comment
  (r/routes router)
  )