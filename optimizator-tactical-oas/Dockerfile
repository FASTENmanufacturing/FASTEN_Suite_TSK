FROM openjdk:8

COPY target/optimizator-tactical-oas-0.0.1-SNAPSHOT.jar optimizator-tactical-oas.jar

EXPOSE 5050

ENV JAVA_OPTS ""

CMD java ${JAVA_OPTS} -jar optimizator-tactical-oas.jar