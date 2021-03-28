FROM openjdk:15.0-buster
COPY ./target/spring-boot-essentials-0.0.1-SNAPSHOT.jar /app
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-essentials-0.0.1-SNAPSHOT.jar"]