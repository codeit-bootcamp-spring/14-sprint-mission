package com.sprint.mission.discodeit.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreateRequestDto {
    private final String username;
    private final String email;
    private final String password;

    // 팩토리 패턴
    public static UserCreateRequestDto of(String username, String email, String password) {

        if (username == null || username.isEmpty()) {
            throw new RuntimeException("User의 username이 비어있습니다.");
        }
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("User의 email이 비어있습니다.");
        }
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("User의 password가 비어있습니다.");
        }
        return new UserCreateRequestDto(username, email, password);
    }
}
