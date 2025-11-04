# builder stage
FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -DskipTests
COPY src ./src
RUN mvn package -DskipTests

# run stage
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/backend-app-0.0.1.jar .
ENTRYPOINT ["java", "-jar", "backend-app-0.0.1.jar"]