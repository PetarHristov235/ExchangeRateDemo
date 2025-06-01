#Start with a base image containing Java runtime
FROM eclipse-temurin:17-jdk-alpine

# Information around who maintains the image
MAINTAINER petarHristov.com

# Add the application's jar to the image
COPY build/libs/currency-exchange-project-0.0.1-SNAPSHOT.jar currency-exchange-project-0.0.1-SNAPSHOT.jar

#Execute the application
ENTRYPOINT ["java", "-jar", "currency-exchange-project-0.0.1-SNAPSHOT.jar"]