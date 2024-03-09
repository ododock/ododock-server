FROM openjdk:17-jdk-slim AS builder
COPY . /ododock
WORKDIR /ododock
RUN chmod +x gradlew
RUN ./gradlew clean build

FROM openjdk:17-jdk-slim
COPY --from=builder /ododock/build/libs/web-server-0.0.1-SNAPSHOT.jar /app/web-server-0.0.1-SNAPSHOT.jar
WORKDIR /app
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "web-server-0.0.1-SNAPSHOT.jar"]