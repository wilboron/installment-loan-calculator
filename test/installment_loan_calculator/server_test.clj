(ns installment-loan-calculator.server-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [muuntaja.core :as m]
            [schema.core :as s])
  (:use [installment-loan-calculator.conftest :only [app-test]]))

(s/set-fn-validation! true)

(def installments
  [{:balance   "3413.96"
    :interest  "250.00"
    :payday    "02/10/2021"
    :payment   "1836.04"
    :period    "1"
    :principal "1586.04"}
   {:balance   "1748.61"
    :interest  "170.70"
    :payday    "01/11/2021"
    :payment   "1836.04"
    :period    "2"
    :principal "1665.34"}
   {:balance   "0.00"
    :interest  "87.43"
    :payday    "01/12/2021"
    :payment   "1836.04"
    :period    "3"
    :principal "1748.61"}])

(def calculate-loan-response
  {:annual-interest-rate         "79.5856%"
   :balance                      "5508.13"
   :capital                      "5000"
   :installments                 installments
   :interest                     "508.13"
   :month-interest-rate          "5.0000%"
   :nominal-annual-interest-rate "60.0000%"})

(deftest server-test
  (testing "Success: Health route"
    (let [response (app-test (mock/request :get "/health"))
          status (get response :status)
          headers (get response :headers)
          body (m/decode "application/json" (get response :body))]
      (is (= status 200))
      (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
      (is (= body {}))))

  (testing "calculate-loan route"
    (testing "Success: Loan with 3 terms"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-loan")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" 5,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 200))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= body calculate-loan-response))))
    (testing "Error: Loan with 0 terms"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-loan")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" 5,
                                                  "loan_term"     0})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:loan_term "(not (\"Must be zero or positive number\" 0))"}))))
    (testing "Error: Loan with 0 interest_rate"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-loan")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" 0,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:interest_rate "(not (\"Must be a number between 0 and 100 inclusive\" 0))"}))))
    (testing "Error: Loan with 0 principal"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-loan")
                                 (mock/json-body {"principal"     0,
                                                  "interest_rate" 5,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:principal "(not (\"Must be zero or positive number\" 0))"}))))
    (testing "Error: Loan with -1 terms"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-loan")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" 5,
                                                  "loan_term"     -1})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:loan_term "(not (\"Must be zero or positive number\" -1))"}))))
    (testing "Error: Loan with -1 interest_rate"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-loan")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" -1,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:interest_rate "(not (\"Must be a number between 0 and 100 inclusive\" -1))"}))))
    (testing "Error: Loan with -1 principal"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-loan")
                                 (mock/json-body {"principal"     -1,
                                                  "interest_rate" 5,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:principal "(not (\"Must be zero or positive number\" -1))"})))))
  (testing "calculate-loan route"
    (testing "Success: Loan with 3 terms"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-amortization-table")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" 5,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 200))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= body installments))))
    (testing "Error: Loan with 0 terms"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-amortization-table")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" 5,
                                                  "loan_term"     0})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:loan_term "(not (\"Must be zero or positive number\" 0))"}))))
    (testing "Error: Loan with 0 interest_rate"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-amortization-table")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" 0,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:interest_rate "(not (\"Must be a number between 0 and 100 inclusive\" 0))"}))))
    (testing "Error: Loan with 0 principal"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-amortization-table")
                                 (mock/json-body {"principal"     0,
                                                  "interest_rate" 5,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:principal "(not (\"Must be zero or positive number\" 0))"}))))
    (testing "Error: Loan with -1 terms"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-amortization-table")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" 5,
                                                  "loan_term"     -1})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:loan_term "(not (\"Must be zero or positive number\" -1))"}))))
    (testing "Error: Loan with -1 interest_rate"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-amortization-table")
                                 (mock/json-body {"principal"     5000,
                                                  "interest_rate" -1,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:interest_rate "(not (\"Must be a number between 0 and 100 inclusive\" -1))"}))))
    (testing "Error: Loan with -1 principal"
      (let [response (app-test (->
                                 (mock/request :post "/calculate-amortization-table")
                                 (mock/json-body {"principal"     -1,
                                                  "interest_rate" 5,
                                                  "loan_term"     3})))
            status (get response :status)
            headers (get response :headers)
            body (m/decode "application/json" (get response :body))]
        (is (= status 400))
        (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
        (is (= (:errors body) {:principal "(not (\"Must be zero or positive number\" -1))"}))))))



