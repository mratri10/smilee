# Use a base image
FROM adoptopenjdk/openjdk17:alpine-slim

# Expose a port (optional)
EXPOSE 2707

# Copy the JAR file into the container
COPY target/puscerdas.jar puscerdas.jar

# Define the command to run your application
CMD ["java", "-jar", "/puscerdas.jar"]