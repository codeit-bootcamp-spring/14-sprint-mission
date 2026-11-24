# 0-spring-mission

스프린트 미션 모범 답안 리포지토리입니다.

[![codecov](https://codecov.io/gh/codeit-bootcamp-spring/0-sprint-mission/branch/s8%2Fadvanced/graph/badge.svg?token=XRIA1GENAM)](https://codecov.io/gh/codeit-bootcamp-spring/0-sprint-mission)

## AWS S3 통합 테스트

기본 `./gradlew test`는 실제 AWS를 호출하지 않습니다. `.env`에 `AWS_S3_ACCESS_KEY`, `AWS_S3_SECRET_KEY`, `AWS_S3_REGION`, `AWS_S3_BUCKET`을 설정한 뒤 `./gradlew s3IntegrationTest`로 업로드·다운로드·Presigned URL 테스트를 실행합니다. 테스트는 지정한 버킷에 임시 객체를 생성하고 삭제합니다. `.env`와 키는 커밋하지 않습니다.

## Spring Boot 4.1.1 마이그레이션

Java 17과 Gradle Wrapper를 사용합니다. `./gradlew clean test`는 S3 통합 테스트를 제외하고 실행하며, Swagger/OpenAPI 공개 접근도 MockMvc로 검증합니다.

이번 변경은 버전 호환성에 한정합니다. 기존 `ReadStatusApiIntegrationTest.createReadStatus_Failure_Duplicate`의 409 기대와 구현의 201 응답 불일치는 수정하지 않아 해당 테스트 실패가 남아 있습니다. Dockerfile의 기존 `PROJECT_VERSION=1.2-M8`과 프로젝트 버전 `2.0-M9` 불일치도 수정하지 않았으므로 컨테이너 배포 완료 상태가 아닙니다.
