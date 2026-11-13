# Build
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY . .

RUN mvn -B clean package -DskipTests

# Runtime
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "--sun-misc-unsafe-memory-access=allow", \
  "--enable-native-access=ALL-UNNAMED", \
  "-Dio.netty.tryReflectionSetAccessible=true", \
  "-jar", "app.jar"]
