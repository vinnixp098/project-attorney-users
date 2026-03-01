# Stage 1: Build
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copia arquivos de código
COPY pom.xml .
COPY src ./src

# Builda o projeto
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim

WORKDIR /app

EXPOSE 8080

# Copia o JAR gerado no stage de build
COPY --from=build /app/target/sistema-pedidos-users-0.0.1-SNAPSHOT.jar app.jar

# Comando para rodar o app
ENTRYPOINT ["java", "-jar", "app.jar"]