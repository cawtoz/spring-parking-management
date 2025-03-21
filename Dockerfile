# Usa una imagen base más ligera para el build
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia solo los archivos necesarios para resolver dependencias primero (optimiza la caché)
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw

# Descarga las dependencias necesarias antes de copiar todo el código
RUN ./mvnw dependency:resolve

# Copia el código fuente al contenedor
COPY src ./src

# Compila la aplicación sin correr los tests
RUN ./mvnw clean package -DskipTests

# Usa una imagen base ligera para la ejecución
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Expone el puerto
EXPOSE 8080

# Copia el archivo JAR compilado desde la fase de construcción
COPY --from=build /app/target/spring-parking-management-0.0.1-SNAPSHOT.jar app.jar

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
