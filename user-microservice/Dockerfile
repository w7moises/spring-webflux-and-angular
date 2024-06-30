FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/user-microservice-0.0.1-SNAPSHOT.jar user-microservice.jar
ENTRYPOINT ["java","-jar","/user-microservice.jar"]