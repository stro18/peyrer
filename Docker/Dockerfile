# 2-stage-build
# first stage
FROM maven:3.5-jdk-8 AS build
COPY ../modules /usr/src/modules
COPY ../pom.xml /usr/src/pom.xml
# clean target directory and build jars
RUN mvn -f /usr/src/pom.xml clean package

# second stage
FROM openjdk:11-alpine
COPY --from=build /usr/src/modules/indexmodule/target/databasemodule-1.0-SNAPSHOT.jar /usr/app/databasemodule-1.0-SNAPSHOT.jar
COPY --from=build /usr/src/modules/indexmodule/target/indexmodule-1.0-SNAPSHOT.jar /usr/app/indexmodule-1.0-SNAPSHOT.jar
COPY --from=build /usr/src/modules/indexmodule/target/retrievalmodule-1.0-SNAPSHOT.jar /usr/app/retrievalmodule-1.0-SNAPSHOT.jar
COPY --from=build /usr/src/modules/indexmodule/target/mainmodule-1.0-SNAPSHOT.jar /usr/app/mainmodule-1.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/mainmodule-1.0-SNAPSHOT.jar"]