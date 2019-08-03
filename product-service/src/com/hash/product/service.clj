(ns com.hash.product.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [com.hash.product.interceptors.components :as components]
            [com.hash.product.controller :as controller]
            [com.hash.product.util :as util]))

(defn get-products
  "Get user-id from header and send to controller to get all products"
  [{{:keys [db discount-client pagination]} :components
    :keys [headers path-info-with-query-string]}]

  (let [user-id (get headers "x-user-id" "0")
        products (controller/get-products user-id db discount-client pagination)]

    (if products
      (ring-resp/response products)
      (ring-resp/not-found
        (util/error "ResourcesNotFoundError" (str path-info-with-query-string) "Products not found")))))

(defn get-product
  "Get product-id from url and user-id from header and send to controller to get a product"
  [{{:keys [product-id]} :path-params
    {:keys [db discount-client]} :components
    :keys  [headers path-info]}]

  (let [user-id (get headers "x-user-id" "0")
        product (controller/get-product product-id user-id db discount-client)]
    (if product
      (ring-resp/response product)
      (ring-resp/not-found
        (util/error "ResourceNotFoundError" path-info "Product id not found")))))

;; Common interceptors
;; Functions to manipulate and helpful our views
(def common-interceptors
  [components/hashlab-components
   (body-params/body-params)
   http/json-body])

;; Routes
(def routes #{["/product" :get (conj common-interceptors `get-products)]
              ["/product/:product-id" :get (conj common-interceptors `get-product)]})

;; Consumed by product-service.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env                     :prod
              ::http/routes            routes
              ::http/resource-path     "/public"

              ::http/type              :jetty
              ::http/host             "0.0.0.0"
              ::http/port              8080
              ::http/container-options {:h2c? true
                                        :h2?  false
                                        :ssl? false}})
