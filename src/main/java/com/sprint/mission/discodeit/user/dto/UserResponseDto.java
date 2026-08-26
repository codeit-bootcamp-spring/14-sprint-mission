package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        UUID profileId,
        String username,
        String email,
        boolean online
) {
    public static UserResponseDto from(User user, UserStatus userStatus){
        return new UserResponseDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfileId(),
                user.getUserName(),
                user.getEmail(),
                userStatus.isOnline()
        );
    }
}
