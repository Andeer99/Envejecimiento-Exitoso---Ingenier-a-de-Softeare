# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia todos los archivos necesarios (incluye mvnw, .mvn, pom.xml, src, etc)
COPY . .

# Si usas Windows, esta línea normaliza endings (no hace daño en Linux)
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Construye el JAR omitiendo tests para producción (ajusta si quieres con tests)
RUN ./mvnw clean package -DskipTests=true

# Etapa de producción: usa sólo el JAR construido
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
