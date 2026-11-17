# 1단계: 빌드 (JDK)
FROM amazoncorretto:17 AS builder
WORKDIR /app
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon      # 의존성 레이어 캐시
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# 2단계: 실행 (JRE)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/app.jar app.jar
ENV JVM_OPTS=""
EXPOSE 80
ENTRYPOINT ["sh", "-c", "exec java $JVM_OPTS -jar app.jar"]