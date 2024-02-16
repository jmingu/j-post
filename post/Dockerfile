FROM openjdk:17-alpine

ARG JAR_FILE=/build/libs/j-post.jar

COPY ${JAR_FILE} j-post.jar

ENTRYPOINT ["java","-jar","/j-post.jar"]