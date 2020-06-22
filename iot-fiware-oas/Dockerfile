FROM openjdk:8

COPY target/iot-fiware-oas-0.0.1-SNAPSHOT.jar iot-fiware-oas.jar

EXPOSE 4444

ENV JAVA_OPTS ""

CMD java ${JAVA_OPTS} -jar iot-fiware-oas.jar