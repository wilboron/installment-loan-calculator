(defproject installment_loan_calculator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-jetty-adapter "1.9.4"]
                 [ring/ring-devel "1.9.4"]
                 [metosin/reitit "0.5.13"]
                 [ring-logger "1.0.1"]]
  :repl-options {:init-ns installment-loan-calculator.server}
  :main ^:skip-aot installment-loan-calculator.server)
