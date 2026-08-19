package com.sprint.mission.discodeit.web.controller.dto.res;

import com.sprint.mission.discodeit.domain.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserUpdateResponseDTO(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId
    ) {
    public static UserUpdateResponseDTO of(User user) {
        return new UserUpdateResponseDTO(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getName(),
            user.getEmail(),
            user.getProfileId()
        );
    }
}
