FROM openjdk:11-jdk
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/connect-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} connect.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/connect.jar"]