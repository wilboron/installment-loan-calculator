(ns installment-loan-calculator.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [ring.middleware.reload :as reload]
            [ring.logger :as logger]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.ring :as ring]
            [installment-loan-calculator.routes :as r]
            [reitit.coercion.schema]))

(def app
  "Application with routes and middlewares"
  (ring/ring-handler
    (ring/router
      [r/routes]
      {:data {:coercion   reitit.coercion.schema/coercion
              :muuntaja   m/instance
              :middleware [reload/wrap-reload
                           params/wrap-params
                           muuntaja/format-middleware
                           coercion/coerce-exceptions-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware
                           logger/wrap-with-logger]}})
    (ring/create-default-handler)))

(defn start
  "Start server" []
  (jetty/run-jetty #'app {:port 3000, :join? false})
  (println "server running in port 3000"))

(defn -main []
  (start))
