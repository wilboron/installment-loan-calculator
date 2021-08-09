(ns installment-loan-calculator.schema
  (:require [schema.core :as s]
            [installment-loan-calculator.date-utils :as i.du]))


(def positive-number-error
  "Must be a positive number")

(def positive-number-error
  "Must be zero or positive number")

(def rate-error
  "Must be a number between 0 and 100 inclusive")


(def start-date-error
  "Must be a date in format d/M/yyyy and be in the present or future")


(defn valid-rate?
  "Validate if number is a valid rate
  To be a valid rate num must be a number between 0 and 100 inclusive"
  [number]
  (and (> number 0)
       (<= number 100)))


(defn valid-date?
  "Validate if string is in d/M/yyyy format and the date is in the
  present or future"
  [date-str]
  (some-> date-str
          i.du/str->date
          i.du/present-or-future-date?))

(defn zero-or-positive?
  [number]
  (or (zero? number)
      (pos? number)))


(s/defschema CalculatorBodySchema
  "Define schema for calculator endpoint POST payload.
  Expect a loan_term, principal and interest_rate"
  {:loan_term       (s/constrained s/Int pos? positive-number-error)
   :principal       (s/constrained s/Num pos? positive-number-error)
   :interest_rate   (s/constrained s/Num valid-rate? rate-error)
   (s/optional-key
     :start_date)   (s/constrained s/Str valid-date? start-date-error)
   (s/optional-key
     :grace_period) (s/constrained s/Int zero-or-positive? start-date-error)})
