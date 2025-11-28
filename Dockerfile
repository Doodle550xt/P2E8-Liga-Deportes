FROM amazoncorretto:24-alpine-jdk
COPY target/deportes-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
