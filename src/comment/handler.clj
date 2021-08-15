(ns comment.handler 
  (:require
   [reitit.core :as r]
   [reitit.ring :as ring]
   [ring.adapter.jetty :as jetty]))

(def routes
  [["/ping" {:get (fn [req] {:status 200 :body "ok"})}] ; fn replace lamda char
   ])

(def router
  (ring/router routes))

(def app
  (ring/ring-handler router))

(defn start []
  (jetty/run-jetty #'app {:port 3000 :join? false})
  (println "server running on port 3000")
  )

(comment
  (app {:request-method :get :uri "/ping"})
  (start)
  )

; archive
(comment
  (r/routes router)

  (r/match-by-path router "/ping")
  (r/match-by-path router "/api/users/333")

  (r/match-by-name router ::ping)

  (r/routes router)
  )