(ns comment.handler 
  (:require
   [reitit.core :as r]
   [reitit.ring :as ring]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [muuntaja.core :as m]
   [ring.adapter.jetty :as jetty]))

(def routes
  [["/ping"
    {:get {:handler (fn [req] {:status 200 :body {:a "ok"}})}}] ; fn replace lamda char
   ["/swagger.json"
    {:get {:handler (swagger/create-swagger-handler)
                      ;response (handler req)
                      ;(def r response)
                      ;response
           }}]
   ])

(def router
  (ring/router routes
               {:data {:muuntaja m/instance
                       :middleware [muuntaja/format-middleware
                                    ;;muuntaja/format-negotiate-middleware
                                    ;;muuntaja/format-request-middleware
                                    ;;muuntaja/format-response-middleware
                                    ]}}))

(def app
  (ring/ring-handler router
                     (ring/routes
                       (swagger-ui/create-swagger-ui-handler
                         {:path "/"}))))

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