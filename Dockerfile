EXPOSE 2707
ADD target/puscerdas.jar puscerdas.jar
ENTRYPOINT ["java", "-jar", "/puscerdas.jar"]
