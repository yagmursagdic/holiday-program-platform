FROM openjdk:21-jdk-slim
COPY build/libs/*-all.jar term-service.jar
ENTRYPOINT ["java", "-jar", "/term-service.jar"]