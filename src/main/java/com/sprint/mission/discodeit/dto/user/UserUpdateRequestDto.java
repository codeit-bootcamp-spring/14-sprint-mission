package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    private final UUID id;
    private final String username;
    private final String email;
    private final String password;

    // 객체를 만들고 반환
    public static UserUpdateRequestDto of(UUID id, String username, String email, String password) {
        if (id == null) {
            throw new CustomException(ExceptionType.USER_ID_IS_NULL);
        }
        if (username == null || username.isEmpty()) {
            throw new CustomException(ExceptionType.USER_USERNAME_IS_NULL);
        }
        if (email == null || email.isEmpty()) {
            throw new CustomException(ExceptionType.USER_EMAIL_IS_NULL);
        }
        if (password == null || password.isEmpty()) {
            throw new CustomException(ExceptionType.USER_PASSWORD_IS_NULL);
        }

        return new UserUpdateRequestDto(
                id,
                username,
                email,
                password
                );
    }
}
