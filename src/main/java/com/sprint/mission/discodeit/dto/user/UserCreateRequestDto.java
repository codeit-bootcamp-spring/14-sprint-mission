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

    public static UserCreateRequestDto of(String username, String email, String password) {
        return new UserCreateRequestDto(username, email, password);
    }
}
