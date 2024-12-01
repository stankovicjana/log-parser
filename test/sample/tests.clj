(ns sample.tests
  (:require [midje.sweet :refer :all]))

(fact "test"
      (+ 1 2) => 3)