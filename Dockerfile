# --------------------------------------------
# Stage 1: Build the application with Gradle
# --------------------------------------------
FROM gradle:9.2.1-jdk21 AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper & build files first to cache dependencies
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

# Pre-download dependencies to cache them
RUN gradle clean build --no-daemon --write-locks || true

# Copy the rest of the source code
COPY . .

# Build the Spring Boot jar
RUN gradle clean bootJar --no-daemon --stacktrace --info

# --------------------------------------------
# Stage 2: Runtime
# --------------------------------------------
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
