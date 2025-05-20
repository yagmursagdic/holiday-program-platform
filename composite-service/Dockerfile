FROM openjdk:21-jdk-slim
COPY build/libs/*-all.jar composite-service.jar
ENTRYPOINT ["java", "-jar", "/composite-service.jar"]