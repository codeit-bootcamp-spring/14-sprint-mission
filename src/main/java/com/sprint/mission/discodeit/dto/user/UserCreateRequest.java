package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UserCreateRequest(

    @NotBlank(message = "이름을 비워둘 수 없습니다.")
    String userName,
    @NotEmpty(message = "이메일을 비워둘 수 없습니다.")
    String email,
    @NotEmpty(message = "비밀번호를 비워둘 수 없습니다.")
    String password,
    @NotEmpty(message = "닉네임을 비워둘 수 없습니다.")
    String nickName
) {
}

/*
 @NotBlank : 빈값도 허용 안함 <- 보통 이걸 많이씀.
 @NotEmpty : 빈값 허용

'record'란? 데이터를 담기 위한 데이터 읽기 전용 클래스!
DTO만들때 많이 사용함. setter 없음 / 불변 객체임
필드는 private final로 만들어짐 / 읽기메소드는 public으로 해서 조회가능!

특징 : getter 메서드 포함,
@AllArgsConstructor와 비슷한 전체 필드를 받는 생성자O(기본생성자X) <- 기본생성자+setter로 값 채워넣기 불가
equals,toString, hashCode(객체를 숫자로 바꿔주는 메서드) 자동 제공~

!주의! :  UserResponse response = new UserResponse(id, "김나라", 20);에서
꺼내쓸때 getAge(), getName(), getAge()...이렇게 쓰는게 아니라

response.id();
response.name();
response.age();

이런식으로 써야함. 명칭이 다름 ~

기본적으로 객체를 만들 때 필요한 데이터를 한번에 넣고, 이후에 변경하지않는다라는 스타일

기본으로 제공하는 생성자 말고도 이런식으로도 직접 생성자를 추가할 수있음 :

상태) public record User(
    String name,
    Integer age
) {
    public User(String name) {
        this(name, 0);
    }
}

그 후)
new User("철수");    // 가능 --- 철수만 값넣고 생성
new User("철수", 20); // 가능 --- 이름,나이만 넣고 생성

* hashCode()란?
- 객체를 해시 자료구조에서 빠르게 찾기 위한 숫자로 변환해주는 메서드
user.hashCode();하면 정수 숫자가 나옴
객체를 빠르게 비교하거나 찾기 위해서
HashSet같은 자료구조는 객체넣을때 자료 빨리 찾으려고 hashCode()를 사용함.

 */
