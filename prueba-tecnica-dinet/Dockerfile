#FROM java:8
FROM adoptopenjdk/openjdk11:alpine-jre
LABEL maintainer="info@javabydeveloper.com"
WORKDIR /myapp
COPY target/Spring-Boot-jpa-h2-example-0.0.1-SNAPSHOT.jar /myapp/my-app.jar
ENTRYPOINT ["java","-jar","my-app.jar"]