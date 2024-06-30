FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/product-microservice-0.0.1-SNAPSHOT.jar product-microservice.jar
ENTRYPOINT ["java","-jar","/product-microservice.jar"]