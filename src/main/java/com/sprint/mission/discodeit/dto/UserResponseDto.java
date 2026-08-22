package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public record UserResponseDto(
    UUID id,
    String name,
    String email,
    UUID profileId,
     boolean onlined
) {

    public static UserResponseDto from(User user, boolean onlined){
        return new UserResponseDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getProfileId(),
            onlined
        );
    }
}
