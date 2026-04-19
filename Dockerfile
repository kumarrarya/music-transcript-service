# ---------- BUILD STAGE ----------
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Copy parent pom
COPY pom.xml .

# Copy module poms (IMPORTANT for multi-module)
COPY music-transcript-web/pom.xml music-transcript-web/pom.xml
# If you have more modules, add them like:
# COPY module-name/pom.xml module-name/pom.xml

# Download dependencies (cached)
RUN ./mvnw dependency:go-offline -DskipTests

# Copy full project (VERY IMPORTANT)
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy jar from module
COPY --from=builder /build/music-transcript-web/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
