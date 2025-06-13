# Mutli-stage build
# 1. Gradle-build
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# copy source into the container with owner gradle
COPY --chown=gradle:gradle . .

# run the build without daemon
RUN ./gradlew build --no-daemon

# 2. Runtime image
FROM openjdk:21-jdk-slim
WORKDIR /app

# copy build-artefacts from build-container to runtime-container
COPY --from=builder /app/build/libs/*-all.jar app.jar

# run that shit
ENTRYPOINT ["java", "-jar", "app.jar"]