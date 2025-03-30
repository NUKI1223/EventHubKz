FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .env .env
COPY target/EventHubKz-0.0.1-SNAPSHOT.jar  EventHubKz-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "EventHubKz-0.0.1-SNAPSHOT.jar"]

