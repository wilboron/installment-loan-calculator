(ns installment-loan-calculator.handlers
  (:require [installment-loan-calculator.logic :as i.l]
            [installment-loan-calculator.date-utils :as i.du]))

(defn- get-start-date-param
  "Get start_date param and convert to LocalDate object.
  If params does not exist return current date"
  [body]
  (i.du/str->date (get body :start_date (i.du/current-date-as-string))))

(defn health-handler [_]
  {:status 200
   :body   {}})

(defn calculate-amortization-table
  [{{body :body} :parameters}]
  (let [principal (:principal body)
        interest-rate (/ (:interest_rate body) 100.0)
        loan-term (:loan_term body)
        start-date (get-start-date-param body)]
    {:status 200
     :body   (-> (i.l/amortization-schedule principal interest-rate loan-term start-date)
                 i.l/format-to-string-amortization-schedule)}))

(defn calculate-loan
  [{{body :body} :parameters}]
  (let [principal (:principal body)
        interest-rate (/ (:interest_rate body) 100.0)
        loan-term (:loan_term body)
        start-date (get-start-date-param body)]
    {:status 200
     :body   (i.l/installment-loan-simulation principal
                                              interest-rate
                                              loan-term
                                              start-date)}))

