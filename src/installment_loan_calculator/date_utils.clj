(ns installment-loan-calculator.date-utils
  (:require [java-time :as jt]
            [schema.core :as s])
  (:import (java.time.format DateTimeParseException)
           (java.time LocalDate)))

(s/def COMMERCIAL-MONTH-DAYS "30" 30)

(s/defn str->date :- LocalDate
  "Convert a local string in format d/M/yyyy to java.time.LocalDate.
  If cannot convert return false"
  [date-str :- s/Str]
  (try
    (jt/local-date "d/M/yyyy" date-str)
    (catch DateTimeParseException _
      false)))


(s/defn date->str :- s/Str
  "Convert java.time.LocalDate to string in format dd/MM/yyyy"
  [date-str :- LocalDate]
  (jt/format "dd/MM/yyyy" date-str))


(s/defn present-or-future-date? :- s/Bool
  "Validate if LocalDate object is in the present or future"
  [date :- LocalDate]
  (when date
    (or
      (= date (jt/local-date))
      (jt/after? date (jt/local-date)))))


(s/defn current-date-as-string :- s/Str
  "Get current date as string in format dd/MM/yyyy"
  []
  (date->str (jt/local-date)))

(s/defn start-date-with-grace-period :- LocalDate
  "Calculate start date of the loan considering the
  grace period in commercial month."
  [start-date :- LocalDate, grace-period :- s/Int]
  (jt/plus start-date (jt/days (* COMMERCIAL-MONTH-DAYS grace-period))))


(s/defn next-commercial-month :- LocalDate
  "Given a LocalDate get the next commercial month"
  [date :- LocalDate]
  (jt/plus date (jt/days COMMERCIAL-MONTH-DAYS)))
