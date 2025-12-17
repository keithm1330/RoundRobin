# Stage 1: Build the application using Gradle with Java 21
FROM gradle:jdk21 AS builder
WORKDIR /app

# Copy project files
COPY . .

# Build the Spring Boot jar
RUN gradle clean build --no-daemon

# Stage 2: Run the application using Java 21
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
