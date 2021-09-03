# Dockerfile.
FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/mobile-app-ws.jar mobile-app-ws.jar
ENTRYPOINT ["java","-jar","mobile-app-ws.jar"]