# Amazon Corretto 17 베이스 이미지
FROM amazoncorretto:17

# gradlew 스크립트가 쓰는 xargs가 이 이미지(Amazon Linux 2023)에는 없어서 설치
RUN dnf install -y findutils && dnf clean all

# 작업 디렉토리
WORKDIR /app

# 프로젝트 파일 복사 (.dockerignore로 불필요한 파일 제외)
COPY . .

# Gradle Wrapper로 실행 가능한 jar 빌드
RUN chmod +x ./gradlew && ./gradlew bootJar --no-daemon

# 80 포트 노출 (prod 프로필의 server.port)
EXPOSE 80

# jar 이름 추론에 쓰는 프로젝트 정보와 JVM 옵션
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# exec 형식에서는 ${...}가 치환되지 않으므로 sh -c로 실행
ENTRYPOINT ["sh", "-c", "exec java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
