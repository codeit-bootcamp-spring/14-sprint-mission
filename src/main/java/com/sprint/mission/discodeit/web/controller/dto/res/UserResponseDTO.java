package com.sprint.mission.discodeit.web.controller.dto.res;

import com.sprint.mission.discodeit.domain.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {
    public static UserResponseDTO of(User user, Boolean isOnline) {
        return new UserResponseDTO(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getName(),
            user.getEmail(),
            user.getProfileId(),
            isOnline
        );
    }
}