(ns installment-loan-calculator.server-test
  (:require [clojure.test :refer :all]
            [installment-loan-calculator.server :refer :all]
            [ring.mock.request :as mock]
            [muuntaja.core :as m]))


(deftest app-test
  (testing "Health route"
    (let [response (app (mock/request :get "/health"))
          status (get response :status)
          headers (get response :headers)
          body (m/decode "application/json" (get response :body))]
      (is (= status 200))
      (is (= headers {"Content-Type" "application/json; charset=utf-8"}))
      (is (= body {})))))


