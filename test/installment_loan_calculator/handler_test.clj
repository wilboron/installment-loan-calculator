(ns installment-loan-calculator.handler-test
  (:require [clojure.test :refer :all]
            [installment-loan-calculator.handlers :refer :all]))


(deftest health-handler-test
  (testing "health handler return 200 status and empty body"
    (is (= {:status 200
            :body   {}}
           (health-handler nil)))))



