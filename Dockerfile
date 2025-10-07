# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Cache deps first for faster rebuilds
COPY pom.xml .
# If you use the Maven Wrapper, copy it too so go-offline works offline:
# COPY mvnw ./
# COPY .mvn .mvn
RUN mvn -q -DskipTests dependency:go-offline

# Copy sources & build
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Run as non-root for security
RUN useradd -m -u 10001 spring && chown -R spring /app
USER spring

# Copy the fat jar (name-agnostic)
COPY --from=build /workspace/target/*.jar /app/app.jar

# Optional hints
EXPOSE 8080
ENV TZ=UTC

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]