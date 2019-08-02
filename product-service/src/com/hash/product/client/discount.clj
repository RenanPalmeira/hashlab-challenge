(ns com.hash.product.client.discount
  (:import (com.hash.proto DiscountRequest Discount DiscountResponse DiscountServiceGrpc DiscountServiceGrpc$DiscountServiceBlockingStub)))

(defn make-discount-request
  [{:keys [product-id user-id]}]
  (-> (DiscountRequest/newBuilder)
      (.setProductId product-id)
      (.setUserId user-id)
      (.build)))

(defn execute-discount-request
  [^DiscountServiceGrpc$DiscountServiceBlockingStub discount-client
   ^DiscountRequest request]
  (.getDiscount discount-client request))

(defn discount-response->discount
  [^DiscountResponse discount-response]
  ^Discount (.getDiscount discount-response))

(defn discount->map
  [^Discount discount]
  {:prc (.getPrc discount)
   :value_in_cents (.getValueInCents discount)})

(defn get-discount
  [discount-client payload]
  (try
    (->> (make-discount-request payload)
         (execute-discount-request discount-client)
         (discount-response->discount)
         (discount->map))
    (catch Exception e nil)))