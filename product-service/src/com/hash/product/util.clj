(ns com.hash.product.util
  (:require [clojure.string :as str])
  (:import (io.grpc ManagedChannelBuilder)
           (java.util UUID)
           (java.net InetSocketAddress Socket)))

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

(defn service-is-alive
  "Ping host and port to return if service is up/down"
  [^String host ^Integer port]
  (try
    (-> (InetSocketAddress. host port)
        (#(.connect (Socket.) % 1000))
        (empty?))
    (catch Exception e false)))

(defn create-channel
  "Utility to create a gRPC channel"
  [host port]
  (try
    (if-let [_ (service-is-alive host port)] ;; check if service is alive before gRPC running to prevent retry's
      (-> (ManagedChannelBuilder/forAddress host port)
          (.usePlaintext)
          (.build)))
    (catch Exception e nil)))

;; DATABASE

(defn parse-uuid
  "A shortcut to parse a value to uuid if possible"
  [value]
  (try
    (UUID/fromString value)
    (catch Exception e nil)))

;; DATABASE PAGINATION

(defn page-offset
  "Calculate offset to SQL queries"
  [page per-page]
  (* (dec page) per-page))

(defn total-pages
  "Calculate total pages available based in total of database"
  [total per-page]
  (-> (if (zero? total) 1 total)
      (/ per-page)
      (Math/ceil)
      (int)))

(defn next-page
  "Calculate next number page"
  [total-pages curr-page]
  (if (< curr-page total-pages)
    (inc curr-page)))

(defn prev-page
  "Calculate previous number page"
  [curr-page]
  (if (> curr-page 1)
    (dec curr-page)))

(defn make-pagination
  "Compose functions of pagination to create pagination map"
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
  "Join path-info and query-string the pedestal don't have native form to get this things in same variables"
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
