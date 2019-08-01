FROM java:8-alpine

ADD target/product-service-0.0.1-SNAPSHOT-standalone.jar /product-service/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/product-service/app.jar"]
