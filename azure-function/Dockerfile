# Use an official Java runtime as a parent image
FROM openjdk:18-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/pedro-kacper-erik-function-1.0-SNAPSHOT.jar /app/pedro-kacper-erik-function.jar

# Run the JAR file
CMD ["java", "-jar", "/app/pedro-kacper-erik-function.jar"]
