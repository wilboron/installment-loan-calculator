(ns installment-loan-calculator.logic)


(defn payment-amount
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
  [balance interest-rate payment-amount]
  (let [interest (* balance interest-rate)
        principal (- payment-amount interest)
        new-balance (- balance principal)]
    {:payment   payment-amount
     :interest  interest
     :principal principal
     :balance   (if (neg? new-balance) 0 new-balance)}))

(defn- amortization-schedule-calculator
  [total-loan interest-rate period payment-amount]
  (loop [balance total-loan
         amortizations []
         current-period 1]
    (if (> current-period period)
      amortizations
      (let [payment-breakdown (payment-breakdown balance interest-rate payment-amount)
            payment-breakdown (assoc payment-breakdown :period current-period)]
        (recur (get payment-breakdown :balance)
               (conj amortizations payment-breakdown)
               (inc current-period))))))


(defn amortization-schedule
  "Create amortization schedule for a loan"
  [total-loan interest-rate period]
  (let [payment-amount (payment-amount total-loan interest-rate period)]
    (amortization-schedule-calculator total-loan interest-rate period payment-amount)))

(defn number->string-with-precision-2
  [number]
  (if (integer? number)
    (str number)
    (format "%.2f" number)))

(defn format-payment-breakdown-to-string
  [payment-breakdown]
  (reduce
    (fn [current-map [k v]]
      (assoc current-map k (number->string-with-precision-2 v)))
    {}
    payment-breakdown))

(defn format-to-string-amortization-schedule
  [amortization-schedule]
  (map
    format-payment-breakdown-to-string
    amortization-schedule))
