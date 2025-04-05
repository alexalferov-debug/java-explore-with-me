FROM amazoncorretto:21-alpine
COPY /stats-server/target/*.jar stat_svc.jar
ENTRYPOINT ["java","-jar","/stat_svc.jar"]