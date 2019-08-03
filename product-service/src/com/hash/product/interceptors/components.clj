(ns com.hash.product.interceptors.components
  (:require [io.pedestal.interceptor :as interceptor]
            [com.hash.product.util :as util]
            [com.hash.product.config :as config]))

;; A simple interceptor to put in request db and discount-client
(def hashlab-components
  (interceptor/interceptor
    {:name  ::hashlab-components
    :enter (fn [context]
             (let [discount-client (util/create-grpc-service (:discount-service (:services config/config)))
                   db (:db config/config)]
               (-> context
                   (assoc-in [:request :components :discount-client] discount-client)
                   (assoc-in [:request :components :db] db))))}))