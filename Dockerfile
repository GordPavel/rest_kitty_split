FROM bellsoft/liberica-openjdk-alpine:17.0.4.1-1
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]