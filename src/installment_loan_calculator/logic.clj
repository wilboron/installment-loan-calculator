(ns installment-loan-calculator.logic
  (:require [java-time :as jt]))

(refer-clojure)
(defn- payment-amount
  "Calculate Payment Amount

  Use the mathematical formula below to calculate the
  Payment Amount for the loan.

          | i x (1 + i)^n|
  R = C x    ---------
          |(1 + i)^n - 1 |

  R = Payment Amount / Annuity Payment
  C = Capital / Present Value
  i = interest rate
  n = period / loan term"
  [total-loan interest-rate period]
  (* total-loan
     (/ (* interest-rate
           (Math/pow (+ 1 interest-rate) period))
        (- (Math/pow (+ 1 interest-rate) period)
           1))))


(defn- payment-breakdown
  "Breakdown a loan installment in payment amount, interest, principal
  and current balance"
  [balance interest-rate payment-amount]
  (let [interest (* balance interest-rate)
        principal (- payment-amount interest)
        new-balance (- balance principal)]
    {:payment   payment-amount
     :interest  interest
     :principal principal
     :balance   (if (neg? new-balance) 0 new-balance)}))

(defn- amortization-schedule-calculator
  [total-loan interest-rate period payment-amount start-date]
  (loop [balance total-loan
         amortizations []
         current-period 1
         current-payday (jt/plus start-date (jt/days 30))]
    (if (> current-period period)
      amortizations
      (let [payment-breakdown (payment-breakdown balance interest-rate payment-amount)
            payment-breakdown (assoc payment-breakdown
                                :period current-period
                                :payday (jt/format "dd/MM/yyyy" current-payday))]
        (recur (get payment-breakdown :balance)
               (conj amortizations payment-breakdown)
               (inc current-period)
               (jt/plus current-payday (jt/days 30)))))))


(defn amortization-schedule
  "Create amortization schedule for a loan"
  [total-loan interest-rate period start-date]
  (let [payment-amount (payment-amount total-loan interest-rate period)]
    (amortization-schedule-calculator total-loan
                                      interest-rate
                                      period
                                      payment-amount
                                      start-date)))

(defn- format-with-precision-2
  "Convert values to string, if float use 2 decimal precision"
  [number]
  (if (float? number)
    (format "%.2f" number)
    (str number)))

(defn- format-payment-breakdown-to-string
  "Format a payment breakdown map number values to string.
  If float use 2 decimal precision."
  [payment-breakdown]
  (reduce
    (fn [current-map [k v]]
      (assoc current-map k (format-with-precision-2 v)))
    {}
    payment-breakdown))

(defn format-to-string-amortization-schedule
  "Format amortization schedule payments breakdown map values to string"
  [amortization-schedule]
  (map
    format-payment-breakdown-to-string
    amortization-schedule))

(defn total-loan-interest
  "Calculate total loan interest that will be paid
  at the end of the loan"
  [amortization-schedule]
  (reduce
    (fn [sum payment-breakdown]
      (+ sum (:interest payment-breakdown)))
    0
    amortization-schedule))

(defn month-rate->annual-rate
  "Convert a month compound rate to a annual-rate"
  [month-rate]
  (- (Math/pow (+ 1 month-rate) 12) 1))

(defn rate->percent
  "Convert rate to its percent representation"
  [rate]
  (format "%.4f%%" (* rate 100)))

(defn installment-loan-simulation
  "Return a complete loan simulation with interests and amortization schedule"
  [principal month-interest-rate period start-date]
  (let [payment-amount (payment-amount principal month-interest-rate period)
        annual-interest-rate (month-rate->annual-rate month-interest-rate)
        amortization-schedule (amortization-schedule-calculator
                                principal
                                month-interest-rate
                                period
                                payment-amount
                                start-date)
        total-loan-interest (total-loan-interest amortization-schedule)
        balance (+ principal total-loan-interest)]
    {
     :capital                      (format-with-precision-2 principal)
     :interest                     (format-with-precision-2 total-loan-interest)
     :balance                      (format-with-precision-2 balance)
     :month-interest-rate          (rate->percent month-interest-rate)
     :annual-interest-rate         (rate->percent annual-interest-rate)
     :nominal-annual-interest-rate (rate->percent (* month-interest-rate 12))
     :installments                 (format-to-string-amortization-schedule
                                     amortization-schedule)}))
