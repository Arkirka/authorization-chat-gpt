FROM eclipse-temurin:17-jdk-alpine
ENV SPRING_BOOT_VERSION=3.0.5
RUN apk update && apk add maven && rm -rf /var/lib/apt/lists/*
RUN mkdir -p /app
WORKDIR /app
ADD . /app
RUN mvn clean package -Dspring.boot.version=${SPRING_BOOT_VERSION}
CMD ["java", "-jar", "target/authorization-0.0.1.jar"]