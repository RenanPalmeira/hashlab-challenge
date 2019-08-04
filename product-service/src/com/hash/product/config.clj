(ns com.hash.product.config
  (:require [environ.core :refer [env]]
            [com.hash.product.util :as util]))

;; Here get env configurations or default hardcoded

;; Convert discont-service-uri to map with host and port
(def discount-service-uri
  (util/service-url->host-port (or (env :hashlab-discount-service-uri) "localhost:50051")))

;; database uri
(def db-connection-uri
  (or (env :hashlab-postgres-connection-uri) "jdbc:postgresql://localhost:5432/hashlab?user=hashlab&password=hashlab"))

;; Map of configurations
(def config {:db         {:connection-uri db-connection-uri}
             :services   {:discount-service discount-service-uri}
             :pagination {:per-page (util/parse-int (or (env :hashlab-production-service-per-page) "20"))}})
