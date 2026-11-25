package com.sprint.mission.discodeit.core.application.user.port.in.dto.response;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentResult;
import com.sprint.mission.discodeit.core.domain.user.User;

import java.util.UUID;

public record UserResult(UUID id,
                         String username,
                         String email,
                         BinaryContentResult binaryContentResult,
                         boolean isOnline,
                         String role) {

    public static UserResult from(User entity, boolean isOnline) {
        return new UserResult(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                BinaryContentResult.from(entity.getProfile()),
                isOnline,
                entity.getRole().name()
        );
    }

    public UserResult withOnline(boolean online) {
        return new UserResult(id, username, email, binaryContentResult, online, role);
    }
}
