package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.user.User;

import java.util.List;
import java.util.UUID;

public record UserDto(UUID id,
                      String username,
                      String email,
                      BinaryContent profile,
                      Boolean online) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile(),
                user.isOnline()
        );
    }

    public static List<UserDto> from(List<User> users) {
        return users.stream()
                .map(UserDto::from)
                .toList();
    }
}
