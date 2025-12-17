# Stage 1: Build the application with Gradle
FROM gradle:9.2.1-jdk21 AS builder
WORKDIR /app

# Copy build files (exclude gradle.properties if not present)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Pre-download dependencies to cache them
RUN gradle clean build --no-daemon --write-locks || true

# Copy the rest of the source code
COPY . .

# Build the Spring Boot jar
RUN gradle clean bootJar --no-daemon --stacktrace --info

# Stage 2: Runtime
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
