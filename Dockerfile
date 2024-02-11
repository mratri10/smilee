# Use a base image
FROM maven:3.9.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM khipu/openjdk17-alpine
# Expose a port (optional)
EXPOSE 2601
# Define the command to run your application
ENTRYPOINT ["java", "-jar", "/puscerdas.jar"]