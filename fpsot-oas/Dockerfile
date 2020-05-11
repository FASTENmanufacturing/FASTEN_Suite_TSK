FROM openjdk:8

COPY target/fpsot-oas-0.0.1-SNAPSHOT.jar fpsot-oas.jar

EXPOSE 3030

ENV JAVA_OPTS ""

CMD java ${JAVA_OPTS} -jar fpsot-oas.jar