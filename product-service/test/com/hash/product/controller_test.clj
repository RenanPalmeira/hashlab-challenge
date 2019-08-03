(ns com.hash.product.controller-test
  (:require [clojure.test :refer :all]
            [mock-clj.core :as m]
            [io.pedestal.test :refer :all]
            [com.hash.product.db :as db]
            [com.hash.product.controller :as c]
            [com.hash.product.client.discount :as dc]))

(deftest product->product-with-discount-test

  (let [fake-discount-client identity]

    (testing "test apply discount"
      (m/with-mock [dc/get-discount {:prc 10}]
         (is (=
               (c/product->product-with-discount {:id "" :price_in_cents 10000} 1 fake-discount-client)
               {:id "" :price_in_cents 10000 :discount {:prc 10 :value_in_cents 1000}}))))

    (testing "test discount not found"
      (m/with-mock [dc/get-discount {}]
         (is (=
               (c/product->product-with-discount {:id "" :price_in_cents 10000} 1 fake-discount-client)
               {:id "" :price_in_cents 10000 :discount nil})))
      (m/with-mock [dc/get-discount nil]
         (is (=
               (c/product->product-with-discount {:id "" :price_in_cents 10000} 1 fake-discount-client)
               {:id "" :price_in_cents 10000 :discount nil}))))))

(deftest get-products-test

  (let [fake-discount-client identity
        fake-db identity
        fake-pagination identity]

    (testing "test get all products"
      (m/with-mock [db/get-all-products [{:id "" :price_in_cents 10000} {:id "" :price_in_cents 10000}]
                    db/page-info {}
                    dc/get-discount {:prc 10}]
         (is (=
               (c/get-products 1 fake-db fake-discount-client fake-pagination)
               {:data [{:id "" :price_in_cents 10000 :discount {:prc 10 :value_in_cents 1000}}
                       {:id "" :price_in_cents 10000 :discount {:prc 10 :value_in_cents 1000}}]
                :links {}}))))

    (testing "test products not found"
      (m/with-mock [db/get-all-products []]
         (is (empty? (c/get-products 1 fake-db fake-discount-client fake-pagination)))))

    (testing "test discount not found"
      (m/with-mock [db/get-all-products [{:id "" :price_in_cents 10000} {:id "" :price_in_cents 10000}]
                    db/page-info {}
                    dc/get-discount {}]
         (is (=
               (c/get-products 1 fake-db fake-discount-client fake-pagination)
               {:data [{:id "" :price_in_cents 10000 :discount nil}
                       {:id "" :price_in_cents 10000 :discount nil}]
                :links {}}))))))

(deftest get-product-test

  (let [fake-discount-client identity
        fake-db identity]

    (testing "test get product"
      (m/with-mock [db/get-product {:id "" :price_in_cents 10000}
                    db/page-info {}
                    dc/get-discount {:prc 10}]
         (is (=
               (c/get-product "product-id" 1 fake-db fake-discount-client)
               {:id "" :price_in_cents 10000 :discount {:prc 10 :value_in_cents 1000}}))))

    (testing "test product not found"
      (m/with-mock [db/get-product nil]
         (is (empty? (c/get-product "product-id" 1 fake-db fake-discount-client)))))

    (testing "test discount not found"
      (m/with-mock [db/get-product {:id "" :price_in_cents 10000}
                    dc/get-discount nil]
         (is (=
               (c/get-product "product-id" 2 fake-db fake-discount-client)
               {:id "" :price_in_cents 10000 :discount nil}))))))