FROM openjdk:17-jdk-slim AS builder
COPY . /ododock
WORKDIR /ododock
RUN chmod 700 ./gradlew
RUN ./gradlew clean build

FROM openjdk:17-jdk-slim
COPY --from=builder /home/gradle/src/build/libs/web-server.jar /app/web-server.jar
WORKDIR /app
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "web-server.jar"]