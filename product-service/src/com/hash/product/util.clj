(ns com.hash.product.util
  (:require [clojure.string :as str])
  (:import (io.grpc ManagedChannelBuilder)
           (java.util UUID)))

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

;; DATABASE

(defn parse-uuid
  "A shortcut to parse a value to uuid if possible"
  [value]
  (try
    (UUID/fromString value)
    (catch Exception e nil)))

;; DATABASE PAGINATION

(defn page-offset
  [page per-page]
  (* (dec page) per-page))

(defn total-pages [total per-page]
  (-> (if (zero? total) 1 total)
      (/ per-page)
      (Math/ceil)
      (int)))

(defn next-page [total-pages curr-page]
  (if (< curr-page total-pages)
    (inc curr-page)))

(defn prev-page [curr-page]
  (if (> curr-page 1)
    (dec curr-page)))

(defn make-pagination
  [request per-page]
  (let [endpoint (:path-info request)
        page (parse-int (:page (:query-params request {}) "1"))]

    {:page-offset  (page-offset page per-page)
     :current-page page
     :endpoint     endpoint
     :per-page     per-page}))

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

(defn make-path-info-with-query-string
  "docstring"
  [request]
  (let [query-string (if (:query-string request) (str "?" (:query-string request)) "")]
    (str (:path-info request) query-string)))

(defn data-resources
  "Common to return a list"
  [values]
  {:data values})

(defn error
  "Common to return error"
  [type url message]
  {:errors [{:type    type
             :message message}]
   :url    url})
