FROM amazoncorretto:17
LABEL authors="codeit"

RUN yum install -y findutils && yum clean all

WORKDIR /app

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=2.0-M9 \
    JVM_OPTS=""

# Gradle 파일 먼저 복사 → 의존성 다운로드 (캐시용)
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon || true

# 소스 복사 → jar 빌드
COPY src ./src
RUN ./gradlew clean bootJar -x test --no-daemon

EXPOSE 80

ENTRYPOINT ["sh", "-c", "exec java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]