FROM amazoncorretto:17 AS builder

RUN yum install -y findutils && yum clean all

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM amazoncorretto:17-alpine

WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=2.0-M9
ENV JVM_OPTS=""

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
