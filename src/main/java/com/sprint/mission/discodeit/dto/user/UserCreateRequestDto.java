package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserCreateRequestDto {
    String username;
    String email;
    String password;

    // 팩토리 패턴
    public static UserCreateRequestDto of(String username, String email, String password) {
        if (username == null || username.isEmpty()) {
            throw new CustomException(ExceptionType.USER_USERNAME_IS_NULL);
        }
        if (email == null || email.isEmpty()) {
            throw new CustomException(ExceptionType.USER_EMAIL_IS_NULL);
        }
        if (password == null || password.isEmpty()) {
            throw new CustomException(ExceptionType.USER_PASSWORD_IS_NULL);
        }
        return new UserCreateRequestDto(username, email, password);
    }
}
