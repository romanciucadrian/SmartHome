FROM openjdk:17-alpine
LABEL maintainer="app.smarthome"
ADD target/smarthome-0.0.1-SNAPSHOT.jar smarthome.jar
ENTRYPOINT ["java", "-jar", "smarthome.jar"]
