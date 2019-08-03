(ns com.hash.product.controller
  (:require [com.hash.product.logic :as logic]
            [com.hash.product.db :as db]
            [com.hash.product.client.discount :as dc]
            [com.hash.product.util :as util]))

(defn product->product-with-discount
  "Convert product to product with field discount"
  [product user-id discount-client]
  (let [{:keys [id price_in_cents]} product
        discount (dc/get-discount discount-client (str id) user-id)]

    (assoc product :discount (logic/make-discount-resource price_in_cents (:prc discount)))))

(defn get-products
  "Get all products of database and hydrate each item with discount-service"
  [user-id db discount-client pagination]
  (let [products (db/get-all-products db pagination)]
    (if (not-empty products)
      (-> products
          (->> (map #(product->product-with-discount % user-id discount-client)))
          (util/data-resources)
          (#(assoc % :links (db/page-info :product db pagination)))))))

(defn get-product
  "Get a single product by product id and hydrate with discount-service"
  [product-id user-id db discount-client]
  (if-let [product (db/get-product product-id db)]
    (product->product-with-discount product user-id discount-client)))
