FROM ubuntu:latest
LABEL authors="alexa"


ENTRYPOINT ["top", "-b"]
# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw clean package -DskipTests=true

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/Envejecimiento-Exitoso-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-jar", "/app/Envejecimiento-Exitoso-0.0.1-SNAPSHOT.jar"]
