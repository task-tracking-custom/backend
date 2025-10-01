FROM maven:3-openjdk-17 as maven
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=maven /app/target/TaskTrackerBackend-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
CMD ["java", "-jar", "/app/TaskTrackerBackend-0.0.1-SNAPSHOT.jar"]