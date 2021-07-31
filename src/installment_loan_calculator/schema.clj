(ns installment-loan-calculator.schema
  (:require [schema.core :as s]))


(def positive-number-error-message
  "Must be a positive number")
(def rate-error-message
  "Must be a number between 0 and 100 inclusive")

(defn valid-rate?
  "Validate if number is a valid rate
  To be a valid rate num must be a number between 0 and 100 inclusive"
  [number]
  (and (> number 0)
       (<= number 100)))

(s/defschema CalculatorBodySchema
  "Define schema for calculator endpoint POST payload.
  Expect a loan_term, principal and interest_rate"
  {:loan_term     (s/constrained s/Int pos? positive-number-error-message)
   :principal     (s/constrained s/Num pos? positive-number-error-message)
   :interest_rate (s/constrained s/Num valid-rate? rate-error-message)})
