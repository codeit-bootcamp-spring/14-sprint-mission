package com.sprint.mission.discodeit.dto.user;

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
        return new UserUpdateRequestDto(
                id,
                username,
                email,
                password
                );
    }
}
