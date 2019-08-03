(ns com.hash.product.logic-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [com.hash.product.logic :as logic]))

(deftest calculate-value-in-cents-test
  (is (=
        (logic/calculate-value-in-cents 10000 5)
        500))
  (is (=
        (logic/calculate-value-in-cents 10100 5)
        505))
  (is (=
        (logic/calculate-value-in-cents 15000 5)
        750))
  (is (=
        (logic/calculate-value-in-cents 10000 10)
        1000))
  (is (=
        (logic/calculate-value-in-cents 10100 10)
        1010))
  (is (=
        (logic/calculate-value-in-cents 15000 10)
        1500)))

(deftest make-discount-resource-test

  (testing "tests with percent and price_in_cents with zero values"
    (is (=
          (logic/make-discount-resource -1 0)
          {:prc 0, :value_in_cents 0}))

    (is (=
          (logic/make-discount-resource 0 -1)
          {:prc -1, :value_in_cents 0}))

    (is (=
          (logic/make-discount-resource 0 0)
          {:prc 0, :value_in_cents 0})))


  (testing "tests with percent and price_in_cents valid"
    (is (=
          (logic/make-discount-resource 10000 10)
          {:prc 10, :value_in_cents 1000}))

    (is (=
          (logic/make-discount-resource 1 10)
          {:prc 10, :value_in_cents 0})))

  (testing "tests with percent and price_in_cents empty"
    (is (=
          (logic/make-discount-resource nil nil)
          nil))

    (is (=
          (logic/make-discount-resource 1 nil)
          nil))

    (is (=
          (logic/make-discount-resource nil 1)
          nil))))