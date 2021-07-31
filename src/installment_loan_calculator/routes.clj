(ns installment-loan-calculator.routes
  (:require [schema.core :as s]
            [reitit.coercion.schema]
            [installment-loan-calculator.handlers :as i.h]
            [installment-loan-calculator.schema :as i.s]))


(def routes
  [["/health" {:responses {200 {:body {}}}
               :get       {:summary "Application health check"
                           :handler i.h/health-handler}}]
   ["/calculate_loan" {:responses  {200 {:body s/Any}}
                       :parameters {:body i.s/CalculatorBodySchema}
                       :post       {:summary "Calculate loan"
                                    :handler i.h/calculate-loan-handler}}]])