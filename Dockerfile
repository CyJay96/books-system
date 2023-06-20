FROM openjdk:17-jdk-slim

LABEL maintainer="Voytko K.V."

WORKDIR /app

COPY ./build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
