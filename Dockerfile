# Use a base image
#FROM maven:3.9.5-openjdk-17 AS build
#COPY . .
#RUN mvn clean package -DskipTests

FROM techiescamp/jre-17:1.0.0
WORKDIR /app
# Copy the JAR file into the container
COPY /target/*.jar ./puscerdas.jar

# Expose a port (optional)
EXPOSE 2808
# Define the command to run your application
CMD ["java", "-jar", "puscerdas.jar"]