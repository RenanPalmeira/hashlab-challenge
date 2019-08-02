(ns com.hash.product.client.discount
  (:import (com.hash.proto DiscountRequest Discount DiscountResponse DiscountServiceGrpc DiscountServiceGrpc$DiscountServiceBlockingStub)))

(defn make-discount-request
  "Create a DiscountRequest from clojure map"
  [{:keys [product-id user-id]}]
  (-> (DiscountRequest/newBuilder)
      (.setProductId product-id)
      (.setUserId user-id)
      (.build)))

(defn execute-discount-request
  "Execute a DiscountRequest to discount-service"
  [^DiscountServiceGrpc$DiscountServiceBlockingStub discount-client
   ^DiscountRequest request]
  (.getDiscount discount-client request))

(defn discount-response->discount
  "Execute getDiscount method of DiscountResponse"
  [^DiscountResponse discount-response]
  ^Discount (.getDiscount discount-response))

(defn discount->map
  "Convert a Discount type to clojure map"
  [^Discount discount]
  {:prc (.getPrc discount)})

(defn get-discount
  "Get a discount with client and payload with use a compose functions
  and if any error return nil to don't stop flow"
  [discount-client payload]
  (try
    (->> (make-discount-request payload)
         (execute-discount-request discount-client)
         (discount-response->discount)
         (discount->map))
    (catch Exception e nil)))