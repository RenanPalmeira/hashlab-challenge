(ns com.hash.product.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]))

(defn get-products
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn get-product
  [{{:keys [product-id]} :path-params
    :as request}]
  (ring-resp/response "Hello World!"))

(def common-interceptors
  [(body-params/body-params)
   http/json-body])

;; Tabular routes
(def routes #{["/product" :get (conj common-interceptors `get-products)]
              ["/product/:product-id" :get (conj common-interceptors `get-product)]})

;; Consumed by product-service.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"

              ::http/type :jetty
              ;;::http/host "localhost"
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})
