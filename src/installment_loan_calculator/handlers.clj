(ns installment-loan-calculator.handlers
  (:require [installment-loan-calculator.logic :as i.l]))


(defn health-handler [_]
  {:status 200
   :body   {}})

(defn calculate-loan-handler
  [{{body :body} :parameters}]
  (let [principal (:principal body)
        interest-rate (/ (:interest_rate body) 100)
        loan-term (:loan_term body)]
    {:status 200
     :body   (-> (i.l/amortization-schedule principal interest-rate loan-term)
                 i.l/format-to-string-amortization-schedule)}))
