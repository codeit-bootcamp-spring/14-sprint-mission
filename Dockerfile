#  Amazon Corretto 17 이미지를 베이스 이미지로 사용하세요.
FROM amazoncorretto:17

# xargs 명령어 사용을 위한 패키지 설치
RUN yum install -y findutils

# 작업 디렉토리를 설정하세요. (/app)
WORKDIR /app

# 프로젝트 파일을 컨테이너로 복사하세요
COPY . .

# Gradle Wrapper를 사용하여 애플리케이션을 빌드하세요.
RUN chmod +x ./gradlew && ./gradlew clean bootJar -x test

# 80 포트를 노출하도록 설정하세요.
EXPOSE 80

# 프로젝트 정보를 환경 변수로 설정하세요.
#   PROJECT_NAME: discodeit
ENV PROJECT_NAME=discodeit
#   PROJECT_VERSION: 1.2-M8
ENV PROJECT_VERSION=1.2-M8
# VM 옵션을 환경 변수로 설정하세요.
#   JVM_OPTS: 기본값은 빈 문자열로 정의
ENV JVM_OPTS=""

# 애플리케이션 실행 명령어를 설정하세요. 이때 환경변수로 정의한 프로젝트 정보를 활용하세요.
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]