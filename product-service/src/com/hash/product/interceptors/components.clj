(ns com.hash.product.interceptors.components
  (:require [io.pedestal.interceptor :as interceptor]
            [com.hash.product.client.discount :as discount]
            [com.hash.product.config :as config]
            [com.hash.product.util :as util]))

;; A simple interceptor to put in request db and discount-client
(def hashlab-components
  (interceptor/interceptor
    {:name  ::hashlab-components
     :enter (fn [context]
              (let [request (:request context)

                    discount-client (discount/create-grpc-service (:discount-service (:services config/config)))
                    db (:db config/config)
                    pagination (util/make-pagination (:request context) (:per-page (:pagination config/config)))
                    path-info-with-query-string (util/make-path-info-with-query-string request)]

                (-> context
                    (assoc-in [:request :components :discount-client] discount-client)
                    (assoc-in [:request :components :db] db)
                    (assoc-in [:request :components :pagination] pagination)
                    (assoc-in [:request :path-info-with-query-string] path-info-with-query-string))))}))