(ns com.hash.product.logic
  (:require [com.hash.product.util :as util]))

(defn calculate-value-in-cents
  "Get a percent in cents of a price"
  [price_in_cents percent]
  (-> (* (util/from-cents price_in_cents) percent)
      (/ 100)
      (util/to-cents)))

(defn make-discount-resource
  "Compose resource to present discount of product"
  [price_in_cents percent]
  (let [values-not-empty (and percent price_in_cents)

        ;; calculate value_in_cents of discount
        value_in_cents (if values-not-empty (calculate-value-in-cents price_in_cents percent))

        discount-body {:prc            percent
                       :value_in_cents value_in_cents}]

    (if values-not-empty discount-body)))