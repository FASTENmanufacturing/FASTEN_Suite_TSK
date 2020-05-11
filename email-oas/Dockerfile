FROM openjdk:8

COPY target/email-oas-0.0.1-SNAPSHOT.jar email-oas.jar

EXPOSE 1883

ENV JAVA_OPTS ""

CMD java ${JAVA_OPTS} -jar email-oas.jar