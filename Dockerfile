FROM openjdk:latest
COPY your-application.jar /usr/src/app/puscerdas-0.0.1-SNAPSHOT.jar
WORKDIR /usr/src/app
CMD ["java", "-jar", "puscerdas-0.0.1-SNAPSHOT.jar"]
