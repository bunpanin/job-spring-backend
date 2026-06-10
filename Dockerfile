FROM ghcr.io/graalvm/jdk-community:25

WORKDIR /app

COPY build/libs/job-spring-backend-0.0.1.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]