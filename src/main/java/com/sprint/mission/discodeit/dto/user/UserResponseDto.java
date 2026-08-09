package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.annotation.Nullable;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        @Nullable
        UUID profileId,
        String userName,
        String email,
        boolean isOnline
) {
    public static UserResponseDto from(User user, UserStatus userStatus){
        return new UserResponseDto(
                user.getId(),
                user.getProfileId(),
                user.getUserName(),
                user.getEmail(),
                userStatus.isOnline()
        );
    }
}
