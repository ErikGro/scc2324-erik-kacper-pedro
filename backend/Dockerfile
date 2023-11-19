FROM tomcat:10.0-jdk17-openjdk

RUN rm -rf /usr/local/tomcat/webapps/*


COPY backend/target/pedro-kacper-erik-backend-1.0.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
