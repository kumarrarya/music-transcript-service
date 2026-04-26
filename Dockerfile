FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy entire project (multi-module safe)
COPY . .

# Build only web module
RUN mvn -pl music-transcript-web -am clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy jar from module
COPY --from=builder /build/music-transcript-web/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]