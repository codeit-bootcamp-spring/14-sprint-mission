package com.sprint.mission.discodeit.entity.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Value;

import java.util.UUID;

@Value
public class UserResponseDto {
    UUID id;
    String name;
    String email;
    Boolean isOnline;

    public static UserResponseDto of(User user, UserStatus userStatus) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                userStatus.isOnline()
        );
    }
}
