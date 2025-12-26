# Build stage
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN gradle dependencies --no-daemon || return 0

COPY src ./src

RUN ./gradlew clean build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# Health check
RUN apk add --no-cache curl

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]