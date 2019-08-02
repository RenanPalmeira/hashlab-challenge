(ns com.hash.product.util
  (:import (io.grpc ManagedChannelBuilder)
           (java.util UUID)))

(defn create-channel
  "Utility to create a gRPC channel"
  [host port]
  (-> (ManagedChannelBuilder/forAddress host port)
      (.usePlaintext true)
      (.build)))

(defn parse-uuid [value]
  "A shortcut to parse a value to uuid if possible"
  (try
    (UUID/fromString value)
    (catch Exception e nil)))

(defn resources
  [values]
  "Common to return a list"
  {:count (count values)
   :data values})

(defn error [type url message]
  "Common to return error"
  {:errors [{:type    type
             :message message}]
   :url    url})
