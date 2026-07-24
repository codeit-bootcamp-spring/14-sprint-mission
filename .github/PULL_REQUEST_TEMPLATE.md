## 변경 개요

<!-- 무엇을 왜 변경했는지 간단히 작성해 주세요. -->

## 주요 변경 사항

-
-

## 적용 차수

- [ ] 1차: JCF 기반 도메인·서비스 구현
- [ ] 2차: File IO·Repository·Basic 서비스 구현

## 요구사항 확인

<!-- 완료한 항목만 체크하고, 미충족하거나 다르게 구현한 항목은 아래에 이유를 작성해 주세요. -->

### 1차 요구사항

#### 프로젝트 초기화

- [ ] IntelliJ를 통해 다음 조건으로 Java 프로젝트를 생성합니다.
  - [ ] IntelliJ에서 제공하는 프로젝트 템플릿 중 Java를 선택합니다.
  - [ ] 프로젝트 경로를 스프린트 미션 리포지토리 경로와 같게 설정합니다.
  - [ ] Create Git Repository 옵션을 체크하지 않습니다.
  - [ ] Build system은 Gradle, Gradle DSL은 Groovy를 사용합니다.
  - [ ] JDK 17을 선택합니다.
  - [ ] GroupId는 `com.sprint.mission`으로 설정합니다.
  - [ ] ArtifactId는 수정하지 않습니다.
  - [ ] `.gitignore`에 `.idea/`를 추가합니다.

> 예: 리포지토리 경로가 `/some/path/1-sprint-mission`이면 Name은
> `1-sprint-mission`, Location은 `/some/path`로 설정합니다.

#### 도메인 모델링

- [ ] 디스코드 서비스를 활용해 각 도메인 모델에 필요한 정보를 도출하고 Java Class로 구현합니다.
- [ ] 패키지명은 `com.sprint.mission.discodeit.entity`를 사용합니다.
- [ ] 도메인 모델을 정의합니다.
  - [ ] 공통 필드를 정의합니다.
    - [ ] `id`: 객체 식별자로 `UUID` 타입을 사용합니다.
    - [ ] `createdAt`, `updatedAt`: 생성·수정 시간을 나타내는 유닉스 타임스탬프로 `Long` 타입을 사용합니다.
  - [ ] `User`를 정의합니다.
  - [ ] `Channel`을 정의합니다.
  - [ ] `Message`를 정의합니다.
- [ ] 생성자를 정의합니다.
  - [ ] `id`는 생성자에서 초기화합니다.
  - [ ] `createdAt`은 생성자에서 초기화합니다.
  - [ ] `id`, `createdAt`, `updatedAt`을 제외한 필드는 생성자 파라미터로 초기화합니다.
- [ ] 메서드를 정의합니다.
  - [ ] 각 필드를 반환하는 Getter를 정의합니다.
  - [ ] 필드를 수정하는 update 메서드를 정의합니다.

#### 서비스 설계 및 구현

- [ ] 도메인 모델별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언합니다.
- [ ] 인터페이스 패키지명은 `com.sprint.mission.discodeit.service`를 사용합니다.
- [ ] 인터페이스 네이밍 규칙은 `[도메인 모델 이름]Service`를 사용합니다.
- [ ] 다음 조건을 만족하는 서비스 인터페이스 구현체를 작성합니다.
  - [ ] 클래스 패키지명은 `com.sprint.mission.discodeit.service.jcf`를 사용합니다.
  - [ ] 클래스 네이밍 규칙은 `JCF[인터페이스 이름]`을 사용합니다.
  - [ ] Java Collections Framework로 데이터를 저장하는 `data` 필드를 `final`로 선언하고 생성자에서 초기화합니다.
  - [ ] `data` 필드로 생성, 조회, 수정, 삭제 메서드를 구현합니다.

#### 메인 클래스 구현

- [ ] `main` 메서드가 선언된 `JavaApplication` 클래스를 선언하고 도메인별 서비스 구현체를 테스트합니다.
  - [ ] 등록
  - [ ] 조회(단건, 다건)
  - [ ] 수정
  - [ ] 수정된 데이터 조회
  - [ ] 삭제
  - [ ] 조회를 통해 삭제되었는지 확인

#### 심화 요구사항: 서비스 간 의존성 주입

- [ ] 도메인 모델 간 관계를 고려한 검증 로직을 추가하고 테스트합니다.
  - 힌트: Message를 생성할 때 연관된 도메인 모델 데이터가 존재하는지 확인합니다.

### 2차 요구사항

#### File IO를 통한 데이터 영속화

- [ ] 다음 조건을 만족하는 서비스 인터페이스 구현체를 작성합니다.
  - [ ] 클래스 패키지명은 `com.sprint.mission.discodeit.service.file`을 사용합니다.
  - [ ] 클래스 네이밍 규칙은 `File[인터페이스 이름]`을 사용합니다.
  - [ ] JCF 대신 File IO와 객체 직렬화를 활용해 메서드를 구현합니다.
- [ ] 객체 직렬화와 역직렬화를 적용합니다.
- [ ] Application에서 서비스 구현체를 `File*Service`로 바꾸어 테스트합니다.

#### 서비스 구현체 분석

- [ ] `JCF*Service`와 `File*Service` 구현체를 비교해 공통점과 차이점을 발견합니다.
- [ ] 비즈니스 로직과 관련된 코드를 식별합니다.
- [ ] 저장 로직과 관련된 코드를 식별합니다.

#### Repository 설계 및 구현

- [ ] 저장 로직과 관련된 기능을 도메인 모델별 인터페이스로 선언합니다.
- [ ] 인터페이스 패키지명은 `com.sprint.mission.discodeit.repository`를 사용합니다.
- [ ] 인터페이스 네이밍 규칙은 `[도메인 모델 이름]Repository`를 사용합니다.
- [ ] 다음 조건을 만족하는 JCF Repository 구현체를 작성합니다.
  - [ ] 클래스 패키지명은 `com.sprint.mission.discodeit.repository.jcf`를 사용합니다.
  - [ ] 클래스 네이밍 규칙은 `JCF[인터페이스 이름]`을 사용합니다.
  - [ ] 기존 `JCF*Service`의 저장 로직을 참고해 구현합니다.
- [ ] 다음 조건을 만족하는 File Repository 구현체를 작성합니다.
  - [ ] 클래스 패키지명은 `com.sprint.mission.discodeit.repository.file`을 사용합니다.
  - [ ] 클래스 네이밍 규칙은 `File[인터페이스 이름]`을 사용합니다.
  - [ ] 기존 `File*Service`의 저장 로직을 참고해 구현합니다.

#### 심화 요구사항: 관심사 분리를 통한 레이어 간 의존성 주입

- [ ] 다음 조건을 만족하는 서비스 인터페이스 구현체를 작성합니다.
  - [ ] 클래스 패키지명은 `com.sprint.mission.discodeit.service.basic`을 사용합니다.
  - [ ] 클래스 네이밍 규칙은 `Basic[인터페이스 이름]`을 사용합니다.
  - [ ] 기존 서비스 구현체의 비즈니스 로직을 참고해 구현합니다.
  - [ ] 필요한 Repository 인터페이스를 필드로 선언하고 생성자로 초기화합니다.
  - [ ] 저장 로직은 Repository 인터페이스 필드를 활용하고 직접 구현하지 않습니다.
- [ ] `Basic*Service` 구현체를 활용해 테스트합니다.
- [ ] `JCF*Repository` 구현체를 활용해 테스트합니다.
- [ ] `File*Repository` 구현체를 활용해 테스트합니다.
- [ ] 기존 `JCF*Service` 또는 `File*Service`와 비교해 차이점을 정리합니다.

## 미충족 또는 다르게 구현한 요구사항

<!-- 미충족한 항목, 현재 상태, 선택 이유와 후속 계획을 작성해 주세요. 없으면 "없음"으로 작성합니다. -->

-

## 검증 결과

- [ ] `./gradlew clean test`
- [ ] JCF 구성 실행
- [ ] File 구성 실행
- [ ] File 저장 후 Repository 및 Service 재생성 조회

```text
검증 명령과 결과를 작성해 주세요.
```

## 리뷰 요청 사항

<!-- 설계 선택, 트레이드오프, 집중해서 검토받고 싶은 부분을 작성해 주세요. -->

-
