(ns installment-loan-calculator.date-utils
  (:require [java-time :as jt]
            [schema.core :as s])
  (:import (java.time.format DateTimeParseException)
           (java.time LocalDate)))


(s/defn str->date :- LocalDate
  "Convert a local string in format d/M/yyyy to java.time.LocalDate.
  If cannot convert return false"
  [date-str :- s/Str]
  (try
    (jt/local-date "d/M/yyyy" date-str)
    (catch DateTimeParseException _
      false)))


(s/defn date->str :- s/Str
  "Convert java.time.LocalDate to string in format d/M/yyyy"
  [date-str :- LocalDate]
  (format "dd/MM/yyyy" date-str))


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