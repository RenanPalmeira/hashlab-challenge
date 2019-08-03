(ns com.hash.product.db
  (:use faker.name
        faker.lorem)
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

(defn get-all-products
  "Extending base-query and use where to get all active products"
  [db pagination]
  (d/query db (-> base-query
                  (helpers/where [:= :status true])
                  (sql/build :offset (:page-offset pagination) :limit (:per-page pagination))
                  (sql/format))))

(defn get-product
  "Extending base-query and use where to get product by id and if is active"
  [product-id db]
  (let [id (util/parse-uuid product-id)
        query (-> base-query
                  (helpers/where [:= :id id] [:= :status true])
                  (sql/format))]
    (and
      (uuid? id)
      (first (d/query db query)))))

(defn page-info
  "docstring"
  [table db pagination]
  (let [per-page (:per-page pagination)
        current-page (:current-page pagination)

        record-count (-> {:select [:%count.id] :from [table]}
                         (sql/format)
                         (#(d/query db %))
                         (first)
                         (#(:count % 0)))

        total-pages (util/total-pages record-count per-page)

        next-page (util/next-page total-pages current-page)
        prev-page (util/prev-page current-page)
        endpoint (str (:endpoint pagination) "?page=")]

    (-> {:self  {:number current-page
                 :href   (str endpoint current-page)}
         :last  {:number total-pages
                 :href   (str endpoint total-pages)}
         :first {:number 1
                 :href   (str endpoint "1")}}
        (cond-> next-page (assoc :next {:number next-page
                                        :href   (str endpoint next-page)}))
        (cond-> prev-page (assoc :previous {:number prev-page
                                            :href   (str endpoint prev-page)})))))
