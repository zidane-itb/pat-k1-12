FROM public.ecr.aws/docker/library/maven:3-amazoncorretto-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21

WORKDIR /opt

RUN mkdir mdw
COPY --from=build /app/target/client-app-0.0.jar /opt/mdw

CMD ["java", "-jar", "/opt/mdw/client-app-0.0.jar"]