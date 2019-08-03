(ns com.hash.product.client.discount
  (:require [io.pedestal.log :as log])
  (:import (com.hash.proto DiscountRequest Discount DiscountResponse DiscountServiceGrpc$DiscountServiceBlockingStub)))

(defn make-discount-request
  "Create a DiscountRequest from clojure map"
  [product-id user-id]
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
  [discount-client product-id user-id]
  (let [{:keys [grpc-client]} discount-client]
    (try
      (->> (make-discount-request product-id user-id)
           (execute-discount-request grpc-client)
           (discount-response->discount)
           (discount->map))
      (catch Exception e
        (log/error :msg (.getMessage e))
        nil))))