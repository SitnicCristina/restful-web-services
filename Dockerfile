# ✅ Step 1: Build the JAR using Maven
FROM openjdk:17-jdk-slim AS build

# ✅ Set the working directory
WORKDIR /app

# ✅ Copy the project files into the container
COPY . .

# ✅ Grant execution permission to Maven wrapper
RUN chmod +x ./mvnw

# ✅ Build the application and apply Liquibase migrations
RUN ./mvnw clean package && ./mvnw liquibase:update

# ✅ Step 2: Run the application using a minimal Java image
FROM openjdk:17-jdk-slim

# ✅ Set the working directory for the runtime container
WORKDIR /app

# ✅ Copy the built JAR from the previous stage
COPY --from=build /app/target/restful-web-services-0.0.1-SNAPSHOT.jar /app.jar

# ✅ Expose the application port
#EXPOSE 8080

# ✅ Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]