FROM openjdk:8

COPY target/database-oas-0.0.1-SNAPSHOT.jar database-oas.jar

EXPOSE 8081

ENV JAVA_OPTS ""

CMD java ${JAVA_OPTS} -jar database-oas.jar