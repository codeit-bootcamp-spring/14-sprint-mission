package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    boolean isOnline
) {
    public static UserResponse from(User user, UserStatus userStatus) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), userStatus.isOnline());
    }
}
