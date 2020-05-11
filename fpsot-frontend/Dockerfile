FROM tomcat:9.0.33-jdk8-openjdk

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/fpsot-frontend-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh","run"]