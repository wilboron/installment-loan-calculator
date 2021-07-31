(ns installment-loan-calculator.routes
  (:require [schema.core :as s]
            [reitit.coercion.schema]
            [installment-loan-calculator.handlers :as h]))


(def routes
  [["/health" {:responses {200 {:body {}}}
               :get       {:summary "Application health check"
                           :handler h/health-handler}}]])