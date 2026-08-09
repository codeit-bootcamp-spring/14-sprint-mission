package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestDto {
    private final String username;
    private final String password;

    public static LoginRequestDto from(String username, String password) {
        if (Objects.isNull(username)) {
            throw new CustomException(ExceptionType.LOGIN_USERNAME_IS_NULL);
        }
        if (Objects.isNull(password)) {
            throw new CustomException(ExceptionType.LOGIN_PASSWORD_IS_NULL);
        }
        return new LoginRequestDto(username, password);
    }
}
