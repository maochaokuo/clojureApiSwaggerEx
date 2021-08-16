(ns comment.handler
  (:require
    [comment.middleware :as mw]
    [muuntaja.core :as m]
    [reitit.coercion.spec]
    [reitit.dev.pretty :as pretty]
    [reitit.ring :as ring]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.exception :as exception]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    ))

-(defn ok [{:keys [db] :as req}]
  (println "db:" db)
  {:status 200 :body "yay"})

(def routes
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "Comment System API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/comments"
    {:swagger {:tags ["comments"]}}
    [""
     {:get  {:summary "Get all comments"
             :handler ok}
      :post {:summary    "Create a new comment"
             :parameters {:body {:name              string?
                                 :slug              string?
                                 :text              string?
                                 :parent-comment-id int?}}
             :responses  {200 {:body string?}}
             :handler    ok}}]
    ["/:slug"
     {:get {:summary    "Get comments by slug"
            :parameters {:path {:slug string?}}
            :handler    ok}}]
    ["/id/:id"
     {:put    {:summary    "Update a comment by the moderator"
               :parameters {:path {:id int?}}
               :handler    ok}
      :delete {:summary    "Delete a comment by the moderator"
               :parameters {:path {:id int?}}
               :handler    ok}}]]
   ])

(defn create-app [db]
  (ring/ring-handler
    (ring/router routes
                 {:exception pretty/exception
                  :data {:db db
                         :coercion   reitit.coercion.spec/coercion
                         :muuntaja   m/instance
                         :middleware [swagger/swagger-feature
                                      muuntaja/format-negotiate-middleware
                                      muuntaja/format-response-middleware
                                      exception/exception-middleware
                                      muuntaja/format-request-middleware
                                      coercion/coerce-request-middleware
                                      coercion/coerce-response-middleware
                                      mw/db
                                      ]}})
    (ring/routes
     (swagger-ui/create-swagger-ui-handler
       {:path "/"}))))

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
