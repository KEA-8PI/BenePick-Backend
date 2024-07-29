# Use an official Gradle image to build the application
FROM gradle:7.6.0-jdk17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew gradlew
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY gradle gradle  

# Copy the rest of the source code
COPY src src
COPY config config

# Run the Gradle build to create the executable JAR file
RUN ./gradlew clean build --no-daemon

# Use an official OpenJDK image as the base for the final image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar benepick-backend.jar

# Expose the application port (change this if your application uses a different port)
EXPOSE 8000

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "benepick-backend.jar"]
