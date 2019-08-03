(ns com.hash.product.server
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.http :as server]
            [io.pedestal.http.route :as route]
            [com.hash.product.service :as service]))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your product-service...")
  (-> service/service
      server/create-server
      server/start))