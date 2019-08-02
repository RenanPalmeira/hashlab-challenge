(ns com.hash.product.util
  (:import (io.grpc ManagedChannelBuilder)
           (java.util UUID)))

(defn create-channel
  [host port]
  (-> (ManagedChannelBuilder/forAddress host port)
      (.usePlaintext true)
      (.build)))

(defn parse-uuid [value]
  (try
    (UUID/fromString value)
    (catch Exception e nil)))

(defn resources
  [values]
  {:count (count values)
   :data values})

(defn error [type url message]
  {:errors [{:type    type
             :message message}]
   :url    url})
