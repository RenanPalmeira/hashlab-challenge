(ns com.hash.product.db
  (:require [honeysql.core :as sql]
            [honeysql.helpers :as helpers]
            [clojure.java.jdbc :as d]
            [com.hash.product.util :as util]))

;; base select to compose with wheres or any SQL command
(def base-query {:select [:id
                          :price_in_cents
                          :title
                          :description]
                 :from   [:product]})

(defn get-all-products
  "Extending base-query and using where to get all active products"
  [db pagination]
  (d/query db (-> base-query
                  (helpers/where [:= :status true])
                  (sql/build :offset (:page-offset pagination) :limit (:per-page pagination))
                  (sql/format))))

(defn get-product
  "Extending base-query and using where to get product by id and if is active"
  [product-id db]
  (let [id (util/parse-uuid product-id)
        query (-> base-query
                  (helpers/where [:= :id id] [:= :status true])
                  (sql/format))]
    (and
      (uuid? id)
      (first (d/query db query)))))

(defn page-info
  "Create pagination info, in other words, links to get next/previous/last page"
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
