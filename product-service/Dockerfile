FROM clojure:openjdk-8-lein AS build-env

WORKDIR /usr/src/product-service

COPY . /usr/src/product-service

RUN lein deps

RUN lein protoc

RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" product-service-1.0.0-standalone.jar

FROM java:8-alpine

WORKDIR /product-service

COPY --from=build-env /usr/src/product-service/product-service-1.0.0-standalone.jar /product-service/product-service-1.0.0-standalone.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/product-service/product-service-1.0.0-standalone.jar"]