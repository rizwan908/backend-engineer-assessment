# Stage 1: Build the application
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean build


# Stage 2: Create a lightweight image to run the application
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/app-0.0.1-SNAPSHOT.jar ./app.jar
CMD ["java", "-jar", "app.jar"]
#