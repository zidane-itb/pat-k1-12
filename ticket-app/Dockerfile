FROM maven:3.8.4-openjdk-8-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:8

WORKDIR /opt

RUN mkdir soap-service
RUN mkdir saved_history
COPY --from=build /app/target/soap-0.0.jar /opt/soap-service

CMD ["java", "-jar", "/opt/soap-service/soap-0.0.jar"]