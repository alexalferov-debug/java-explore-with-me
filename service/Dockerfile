FROM amazoncorretto:21-alpine
COPY /target/*.jar service.jar
ENTRYPOINT ["java","-jar","/service.jar"]