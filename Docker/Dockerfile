# 2-stage-build
# first stage
FROM maven:3.6-openjdk-11 AS build
COPY modules /usr/src/modules
COPY pom.xml /usr/src/pom.xml
# clean target directory and build jars
RUN mvn -f /usr/src/pom.xml -DskipTests clean install

# second stage
# no official openjdk11 alpine image available
FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build /usr/src/modules/databasemodule/target/databasemodule-1.0-SNAPSHOT.jar /usr/app/databasemodule-1.0-SNAPSHOT.jar
COPY --from=build /usr/src/modules/indexmodule/target/indexmodule-1.0-SNAPSHOT.jar /usr/app/indexmodule-1.0-SNAPSHOT.jar
COPY --from=build /usr/src/modules/retrievalmodule/target/retrievalmodule-1.0-SNAPSHOT.jar /usr/app/retrievalmodule-1.0-SNAPSHOT.jar
COPY --from=build /usr/src/modules/mainmodule/target/mainmodule-1.0-SNAPSHOT.jar /usr/app/mainmodule-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/app/mainmodule-1.0-SNAPSHOT.jar"]