# Stage 1: Build the application using Maven
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src
# Package the application skipping tests to speed up the process
RUN mvn clean package -DskipTests

# Stage 2: Create the minimal runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar
# Expose the application port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
