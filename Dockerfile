FROM gradle:8.7.0-jdk21 AS builder

WORKDIR /workspace
COPY . .

RUN ./gradlew openapi3

RUN ./gradlew clean build -x test

RUN mkdir -p src/main/resources/static/docs && \
    cp build/api-spec/openapi3.json src/main/resources/static/docs/

RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /workspace/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]