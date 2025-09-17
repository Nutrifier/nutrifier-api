# Stage 1: Build
FROM gradle:8.7-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle build -x test

# Stage 2: Run
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
