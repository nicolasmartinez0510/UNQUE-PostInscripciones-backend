FROM openjdk:17-alpine

ADD build/libs/postinscripciones-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]