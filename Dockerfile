FROM ghcr.io/graalvm/jdk-community:21
WORKDIR app
ADD ./build/libs/job-spring-backend-0.0.1.jar /app/
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/job-spring-backend-0.0.1.jar"]