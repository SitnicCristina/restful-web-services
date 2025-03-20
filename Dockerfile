# ✅ Step 1: Build the JAR using Maven
FROM openjdk:17-jdk-slim AS build

# ✅ Set the working directory
WORKDIR /app

# ✅ Copy only essential files first (to leverage caching)
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn

# ✅ Copy the rest of the project files
COPY src src

# ✅ Grant execution permission to Maven wrapper
RUN chmod +x ./mvnw

# ✅ Build the Spring Boot application (Skipping tests for faster build)
RUN ./mvnw clean package -DskipTests

# ✅ Step 2: Use a separate, clean image to run the application
FROM openjdk:17-jdk-slim

# ✅ Set the working directory for the runtime container
WORKDIR /app

# ✅ Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# ✅ Expose the application port
EXPOSE 80

# ✅ Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]