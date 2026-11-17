# ---------- 1. 빌드 스테이지 ----------
FROM amazoncorretto:17 AS builder

RUN yum install -y findutils && yum clean all

WORKDIR /app

# (1) 잘 안 바뀌는 파일 먼저: Gradle 래퍼와 빌드 설정
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY admin admin

# (2) 의존성만 미리 다운로드 → 이 레이어가 캐시됨
RUN chmod +x ./gradlew && ./gradlew :dependencies --no-daemon

# (3) 자주 바뀌는 소스는 마지막에
COPY src src
RUN ./gradlew :bootJar -x test --no-daemon


# ---------- 2. 런타임 스테이지 ----------
FROM amazoncorretto:17-alpine

WORKDIR /app

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ./

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar"]