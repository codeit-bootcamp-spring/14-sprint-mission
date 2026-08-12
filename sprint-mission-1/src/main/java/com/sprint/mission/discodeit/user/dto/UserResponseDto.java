package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import java.util.UUID;

public record UserResponseDto(UUID id, String userName, String email, UUID binaryId,
                              UUID userStatusId,
                              boolean online) {

    public static UserResponseDto from(User user, UserStatus userStatus) {
        return new UserResponseDto(
            user.getUserId(),
            user.getUserName(),
            user.getEmail(),
            user.getBinaryId(),
            userStatus.getUserStatusId(),
            userStatus.isOnline()
        );
    }
}
