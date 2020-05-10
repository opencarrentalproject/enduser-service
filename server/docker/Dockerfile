FROM adoptopenjdk/openjdk11:alpine-slim

COPY build/libs/*.jar authorization-service.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/authorization-service.jar"]