# Etapa de build com Maven + Temurin
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .
COPY src ./src

RUN mvn clean package -Dmaven.test.skip=true

# Imagem final com JDK Temurin
FROM eclipse-temurin:25-jdk-alpine

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
