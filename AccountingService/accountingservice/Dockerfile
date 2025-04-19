FROM openjdk:21-jdk-slim
COPY build/libs/*-all.jar accounting-service.jar
ENTRYPOINT ["java", "-jar", "/accounting-service.jar"]