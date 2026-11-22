FROM amazoncorretto:21-alpine

WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=2.0-M9
ENV JVM_OPTS=""

COPY . .
RUN chmod +x gradlew \
    && ./gradlew clean build \
    && cp build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]
