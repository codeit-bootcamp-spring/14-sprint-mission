package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponseDto {
    private final UUID id;
    private final String username;
    private final String email;

    public static LoginResponseDto from(User user) {
        return new LoginResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
