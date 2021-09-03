(ns installment-loan-calculator.conftest
  (:require [ring.middleware.params :as params]
            [ring.middleware.reload :as reload]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.ring :as ring]
            [installment-loan-calculator.routes :as r]
            [reitit.coercion.schema]))

(def app-test
  "Application with routes and middlewares for testing
  REMOVING ONLY logging"
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
                           coercion/coerce-response-middleware]}})
    (ring/create-default-handler)))
