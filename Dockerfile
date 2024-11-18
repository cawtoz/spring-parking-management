FROM openjdk:21-jdk-alpine
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean install -DskipTests
EXPOSE 8080
COPY --from=build /target/spring-parking-management-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
