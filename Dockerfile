FROM amazoncorretto:17

RUN yum install -y findutils && yum clean all

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

EXPOSE 80

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
