FROM openjdk:8

COPY target/iot-kafka-oas-0.0.1-SNAPSHOT.jar iot-kafka-oas.jar

EXPOSE 8082

ENV JAVA_OPTS ""

CMD java ${JAVA_OPTS} -jar iot-kafka-oas.jar