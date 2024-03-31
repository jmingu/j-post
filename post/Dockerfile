FROM openjdk:17-alpine

ENV USER_SERVER = "http://10.0.1.56:8081" \
    DATASOURCE_URL = "jdbc:postgresql://10.0.1.56:5432/post" \
    DATASOURCE_USERNAME = "postgres" \
    DATASOURCE_PASSWORD = "j1234!@#$"

ARG JAR_FILE=/build/libs/j-post.jar

COPY ${JAR_FILE} j-post.jar

ENTRYPOINT ["java","-jar","/j-post.jar"]