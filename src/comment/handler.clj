(ns comment.handler 
  (:require
   [reitit.core :as r]
   [reitit.ring :as ring]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.coercion.spec]
   [reitit.ring.coercion :as coercion]
   [muuntaja.core :as m]
   [ring.adapter.jetty :as jetty]))

(def ok (constantly {:status 200 :body "ok"}))

(def routes
  [["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "Comment System API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/comments"
    {:swagger {:tags ["comments"]}}
    [""
     {:get {:summary "Get all comments"
            :handler ok}
      :post {:summary "Create a new comment"
             :parameters {:body {:name string?
                                 :slug string?
                                 :text string?
                                 :parent-comment-id int?}}
             :handler ok}}]
    ["/:slug"
     {:get {:summary "Get comments by slug"
            :handler ok}}]
    ["/id/:id"
     {:put {:summary "Update a comment by the moderator"
            :handler ok}
      :delete {:summary "Delete a comment by the moderator"
               :handler ok}}]]
   ])

(def router
  (ring/router routes
               {:data {:coercion reitit.coercion.spec/coercion
                       :muuntaja m/instance
                       :middleware [muuntaja/format-middleware
                                    coercion/coerce-request-middleware
                                    coercion/coerce-response-middleware
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
