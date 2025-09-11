# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the local Spring Boot JAR file into the container
COPY ./app/build/libs/*.jar /app/app.jar

# Expose the port the app will run on (default Spring Boot port is 8080)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]