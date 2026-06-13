FROM ghcr.io/graalvm/jdk-community:21 AS build
WORKDIR /app
COPY --from=build /app/build/libs/job-spring-backend-0.0.1.jar /app/job-spring-backend.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/job-spring-backend.jar"]