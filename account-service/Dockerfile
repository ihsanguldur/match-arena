FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=build /src/target/*.jar /app.jar
EXPOSE 8080 9090
ENTRYPOINT ["java", "-jar", "/app.jar"]
