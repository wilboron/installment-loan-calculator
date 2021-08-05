(ns installment-loan-calculator.routes
  (:require [schema.core :as s]
            [reitit.coercion.schema]
            [installment-loan-calculator.handlers :as i.handler]
            [installment-loan-calculator.schema :as i.s]))


(def routes
  [["/health"
    {:responses {200 {:body {}}}
     :get       {:summary "Application health check"
                 :handler i.handler/health-handler}}]
   ["/calculate-amortization-table"
    {:responses  {200 {:body s/Any}}
     :parameters {:body i.s/CalculatorBodySchema}
     :post       {:summary "Calculate only amortization table"
                  :handler i.handler/calculate-amortization-table}}]
   ["/calculate-loan"
    {:responses  {200 {:body s/Any}}
     :parameters {:body i.s/CalculatorBodySchema}
     :post       {:summary "Calculate full loan"
                  :handler i.handler/calculate-loan}}]])