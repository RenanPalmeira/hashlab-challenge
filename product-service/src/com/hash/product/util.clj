(ns com.hash.product.util
  (:require [clojure.string :as str])
  (:import (io.grpc ManagedChannelBuilder)
           (java.util UUID)
           (com.hash.proto DiscountServiceGrpc)))

;; CONFIGURATION

(defn parse-int
  "Parse a string to int type"
  [number-string]
  (try
    (Integer/parseInt number-string)
    (catch Exception e nil)))

(defn service-url->host-port
  "Parse service-url (e.g. localhost:9093) to a map {:host localhost :port 9093}"
  [service-url]
  (zipmap [:host :port] (str/split service-url #":")))

;; gRPC

(defn create-channel
  "Utility to create a gRPC channel"
  [host port]
  (-> (ManagedChannelBuilder/forAddress host port)
      (.usePlaintext)
      (.build)))

(defn create-grpc-service
  "Create a map with grpc client and grpc channel"
  [{:keys [host port]}]
  (let [channel (create-channel host (parse-int port))]
    {:grpc-client (DiscountServiceGrpc/newBlockingStub channel)
     :grpc-channel channel}))

;; DATABASE

(defn parse-uuid
  "A shortcut to parse a value to uuid if possible"
  [value]
  (try
    (UUID/fromString value)
    (catch Exception e nil)))

;; VALUES IN CENTS

(defn to-cents
  "Convert a amount in cents to amount common format"
  [amount]
  (-> (bigdec amount)
      (#(.setScale % 2 1))
      (.movePointRight 2)
      (.intValueExact)))

(defn from-cents
  "Convert a amount common format to cents format"
  [amount]
  (.movePointLeft (bigdec amount) 2))

;; JSON RESPONSES

(defn resources
  "Common to return a list"
  [values]
  {:count (count values)
   :data values})

(defn error
  "Common to return error"
  [type url message]
  {:errors [{:type    type
             :message message}]
   :url    url})
