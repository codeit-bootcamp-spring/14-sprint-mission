# Spring 이전과 이후, 무엇이 다른가

비교 대상은 **2-2 프로젝트(`mission/2-1sprint-mission`)의 `JavaApplication`**이다.
미션 3에서는 Spring이 그 역할을 대신하므로 이 프로젝트에서 삭제했다.

두 클래스가 하는 일은 같다. 유저를 만들고, 채널을 만들고, 메시지를 보낸다.
다른 것은 **객체를 누가 만들고, 누가 끼워 넣는가** 하나뿐이다.

## 코드로 본 차이

**2-2의 JavaApplication (Spring 이전)**

```java
public static void main(String[] args) {
    UserRepository userRepository = new FileUserRepository();
    UserService userService = new BasicUserService(userRepository);
    ...
}
```

main이 세 가지를 전부 결정한다.

1. 무엇을 만들 것인가 (`FileUserRepository`)
2. 무엇에 무엇을 끼울 것인가 (`BasicUserService`에 `userRepository`)
3. 언제 만들 것인가 (이 줄에 도달했을 때)

**DiscodeitApplication (Spring 이후)**

```java
public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
    UserService userService = context.getBean(UserService.class);
    ...
}
```

main은 **"UserService 타입인 것을 달라"**고 요청만 한다. 위 세 가지 결정은 사라진 게 아니라 자리를 옮겼다.

## Bean

Spring이 대신 만들어서 보관하는 객체를 Bean이라고 한다.

`new`로 만든 객체와 클래스가 다른 게 아니다. **누가 그 객체의 생애를 관리하느냐**가 다르다. 이 프로젝트에서는 선언을 이렇게 했다.

```java
@Repository          // FileUserRepository
@Service             // BasicUserService
```

`FileUserRepository`에 `@Repository`를 붙이는 순간, "`UserRepository`가 필요하면 이걸 써라"는 결정이 main에서 클래스 선언으로 옮겨간다.

`JCF*Repository`에는 일부러 붙이지 않았다. 그래서 Bean이 아니고, 지금은 `File*Repository`와 `Basic*Service`만 등록된다. 실행하면 이렇게 확인된다.

```text
UserService 구현체    : BasicUserService
```

## IoC Container

Bean을 만들고, 보관하고, 필요한 곳에 꺼내 주는 주체다. `SpringApplication.run(...)`이 돌려주는 `context`가 그것이다.

IoC(Inversion of Control, 제어의 역전)에서 **역전된 "제어"는 객체를 만들고 연결하는 흐름의 주도권**이다.

| | 만드는 주체 | 흐름 |
| --- | --- | --- |
| 2-2의 JavaApplication | 내 코드 | 내가 필요할 때 `new` 한다 |
| DiscodeitApplication | 컨테이너 | 컨테이너가 미리 만들고, 나는 받아 쓴다 |

## Dependency Injection

`BasicUserService`는 `UserRepository`가 있어야 동작한다. 이 "있어야 하는 것"이 의존성이고, 그것을 **밖에서 넣어 주는 것**이 주입이다.

여기서 중요한 건, **주입 자체는 2-2에서 이미 하고 있었다**는 점이다.

```java
public BasicUserService(UserRepository userRepository) {   // 2-2에서 쓴 그대로
    this.userRepository = userRepository;
}
```

이 생성자는 한 글자도 바꾸지 않았다. 2-2에서는 사람이 `new BasicUserService(new FileUserRepository())`로 넣어 줬고, 지금은 컨테이너가 생성자를 보고 넣어 준다. 생성자가 하나뿐이면 `@Autowired` 없이도 Spring이 알아서 쓴다.

그래서 순서는 이렇게 정리된다.

> 생성자 주입으로 만들어 뒀기 때문에 → Spring으로 옮기는 데 서비스 코드를 고칠 일이 없었다.

DI는 Spring이 만들어 준 개념이 아니라, Spring이 **자동화해 준** 개념이다.

## 그래서 무엇이 좋아졌나

저장소를 JCF로 바꾸고 싶다고 해보자.

- **2-2**: `main`에서 `new FileUserRepository()`를 `new JCFUserRepository()`로 고친다. 사용하는 곳이 늘어나면 그만큼 고친다.
- **미션 3**: 어느 클래스에 `@Repository`가 붙어 있는지만 바꾸면 된다. `main`은 손대지 않는다.

심화 요구사항이 이걸 한 걸음 더 밀어서, **Java 코드를 전혀 바꾸지 않고 `application.yaml` 설정값만으로** 구현체를 고르게 한다.

## 남은 의문 (직접 확인할 것)

- `@Component`, `@Service`, `@Repository`는 기능상 무엇이 다른가? (셋 다 Bean으로 등록되는데 왜 나눠 쓰는가)
- 같은 인터페이스를 구현한 Bean이 둘이면 어떻게 되는가? — `JCFUserRepository`에도 `@Repository`를 붙여 보고 실행하면 답이 나온다.
- 이 프로젝트에 `spring-boot-starter-web`이 필요한가? 콘솔 출력만 하는데 Tomcat이 뜬다.
