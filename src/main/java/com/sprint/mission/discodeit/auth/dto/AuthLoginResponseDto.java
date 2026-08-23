package com.sprint.mission.discodeit.auth.dto;

import java.time.Instant;
import java.util.UUID;

public record AuthLoginResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String password,
        UUID profileId
) {
    public static AuthLoginResponseDto from(UUID id, Instant createdAt, Instant updatedAt, String username, String email,
                                            String password, UUID profileId){

        return new AuthLoginResponseDto(id, createdAt, updatedAt, username, email, password, profileId);
    }
}
