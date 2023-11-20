# Use an OpenJDK base image with JDK 17
FROM openjdk:17

# Copy the built JAR file into the Docker image
COPY target/scc2324-mgt-1.0.jar /app/scc2324-mgt-1.0.jar

# Specify the command to run the JAR file
CMD ["java", "-jar", "/app/scc2324-mgt-1.0.jar"]
