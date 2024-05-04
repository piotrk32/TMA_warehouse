# Use an official Java runtime as a parent image
FROM openjdk:19

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
ADD . /app

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Build the application
RUN ./gradlew build

# Run the application when the container launches
CMD ["java", "-jar", "build/libs/TMA_warehouse.jar"]