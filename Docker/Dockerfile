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
WORKDIR /usr/peyrer
COPY --from=build /usr/src/modules/mainmodule/target/mainmodule-1.0-SNAPSHOT-jar-with-dependencies.jar .
COPY topics ./topics/
ENTRYPOINT ["java","-jar","mainmodule-1.0-SNAPSHOT-jar-with-dependencies.jar"]