package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.user.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID id, Instant createdAt, Instant updatedAt, String username,
                           String email, String password, UUID profiledId) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getProfileId()
        );
    }
}
