#ARG DB_ROOT_FILE_PATH=yc-db/db-root.crt
FROM bellsoft/liberica-openjdk-alpine:17.0.4.1-1
#COPY ${DB_ROOT_FILE_PATH} /yc-db/db-root.crt
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]