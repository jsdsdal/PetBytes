# Build
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY . .

RUN mvn -B clean package -DskipTests


# Runtime
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java",
    "-XX:+UnlockDiagnosticVMOptions",
    "-XX:-UseIntPolyIntrinsics",
    "-XX:UseAVX=0",
    "-XX:TieredStopAtLevel=1",
    "-XX:ErrorFile=/tmp/hs_err_pid%p.log",
    "-jar",
    "app.jar"]
