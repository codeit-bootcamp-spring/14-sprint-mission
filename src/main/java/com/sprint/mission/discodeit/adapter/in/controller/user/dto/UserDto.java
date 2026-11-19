package com.sprint.mission.discodeit.adapter.in.controller.user.dto;

import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.core.domain.user.Role;
import com.sprint.mission.discodeit.core.domain.user.User;

import java.util.UUID;

public record UserDto(UUID id,
                      String username,
                      String email,
                      BinaryContent profile,
                      Boolean online,
                      Role role) {

    public static UserDto from(User user, boolean isOnline) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile(),
                isOnline,
                user.getRole()
        );
    }

    public UserDto withOnline(boolean isOnline) {
        return new UserDto(id, username, email, profile, isOnline, role);
    }
}
