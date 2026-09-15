package com.sprint.mission.discodeit.auth.dto;

import com.sprint.mission.discodeit.user.dto.UserResponseDto.ProfileResponse;

import java.time.Instant;
import java.util.UUID;

public record AuthLoginResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String password,
        ProfileResponse profile
) {
    public static AuthLoginResponseDto from(UUID id, Instant createdAt, Instant updatedAt, String username, String email,
                                            String password, ProfileResponse profile){

        return new AuthLoginResponseDto(id, createdAt, updatedAt, username, email, password, profile);
    }
}
