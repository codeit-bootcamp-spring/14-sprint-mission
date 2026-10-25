FROM amazoncorretto:17-alpine

WORKDIR /app

ENV PROJECT_NAME=discodeit\
    PROJECT_VERSION=1.2-M8\
    JVM_OPTS="" \
    SERVER_PORT=80

COPY . .

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

EXPOSE 80

ENTRYPOINT ["sh", "-c", "exec java $JVM_OPTS -jar /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]

