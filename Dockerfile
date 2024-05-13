# Start from the official Ubuntu base image
FROM ubuntu:latest

# Install OpenJDK 17 and findutils
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk findutils

# Set the working directory
WORKDIR /app

# Copy application files
ADD . /app

# Make the gradlew script executable
RUN chmod +x ./gradlew

# Build the application without running tests and list the output directory
RUN ./gradlew build -x test && ls -la /app/build/libs/

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Ensure the JAR file is executable and specify the full path for CMD
RUN chmod +x /app/build/libs/tma_warehouse-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/build/libs/tma_warehouse-0.0.1-SNAPSHOT.jar"]