(defproject com.hash.product "1.0.0"
  :description "Product service"
  :url "https://github.com/RenanPalmeira/hashlab-challenge"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-protoc "0.5.0"]
            [jonase/eastwood "0.3.5"]]

  :protoc-version "3.9.0"
  :protoc-grpc {:version "1.22.1"}
  :proto-target-path "proto"

  :proto-source-paths ["proto"]
  :java-source-paths ["proto"]

  :dependencies [[org.clojure/clojure "1.10.1"]

                 [io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.jetty "0.5.7"]

                 ;; database
                 [org.clojure/java.jdbc "0.7.9"]
                 [org.postgresql/postgresql "42.2.6"]
                 [honeysql "0.9.4"]

                 ;; gRPC
                 [javax.annotation/javax.annotation-api "1.2"]

                 [com.google.protobuf/protobuf-java "3.9.0"]
                 [com.google.protobuf/protoc "3.9.0" :extension "pom"]

                 [io.netty/netty-codec-http2 "4.1.38.Final"]

                 [io.grpc/grpc-netty "1.22.1" :exclusions [io.netty/netty-codec-http2 io.grpc/grpc-core]]
                 [io.grpc/grpc-protobuf "1.22.1"]
                 [io.grpc/grpc-core "1.22.1" :exclusions [com.google.errorprone/error_prone_annotations io.grpc/grpc-api]]
                 [io.grpc/grpc-stub "1.22.1"]

                 ;; test
                 [faker "0.2.2"]

                 ;; settings
                 [environ "1.1.0"]

                 ;; test
                 [mock-clj "0.2.0"]

                 ;; logging
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]]

  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]

  :profiles {:uberjar {:aot [com.hash.product.server]}}

  :main ^{:skip-aot true} com.hash.product.server)
