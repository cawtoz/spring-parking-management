FROM openjdk:21-jdk AS build
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean install -DskipTests

FROM openjdk:21-jdk
EXPOSE 8080
COPY --from=build /target/spring-parking-management-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
