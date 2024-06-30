FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/order-microservice-0.0.1-SNAPSHOT.jar order-microservice.jar
ENTRYPOINT ["java","-jar","/order-microservice.jar"]
