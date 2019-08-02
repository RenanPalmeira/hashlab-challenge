(ns com.hash.product.db
  (:use faker.name faker.lorem)
  (:require [honeysql.core :as sql]
            [honeysql.helpers :as helpers]
            [clojure.java.jdbc :as d]
            [com.hash.product.util :as util]))

(def base-query {:select [:id
                          :price_in_cents
                          :title
                          :description]
                 :from   [:product]})


(def n (take 100 (names)))
(def description (take 100 (paragraphs)))
(def price (take 100 (iterate inc 1)))
(def create_date (take 100 (repeat (java.sql.Timestamp/valueOf "2004-10-19 10:23:54"))))

(def db {:connection-uri "jdbc:postgresql://localhost:5432/hashlab?user=hashlab&password=hashlab"})

(defn fake-insert
  "docstring"
  [arglist]
  (d/query db (-> (helpers/insert-into :product)
                  (helpers/columns :title :description :price_in_cents :create_date)
                  (helpers/values
                    (map vector n description price create_date))
                  (sql/format))))

(defn get-products
  [db]
  (d/query db (-> base-query
                  (helpers/where [:= :status true])
                  (sql/format))))

(defn get-product
  [db product-id]
  (let [id (util/parse-uuid product-id)
        query (-> base-query
                  (helpers/where [:= :id id] [:= :status true])
                  (sql/format))]
    (and
      (uuid? id)
      (first (d/query db query)))))