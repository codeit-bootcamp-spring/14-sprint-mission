package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserDto(UUID id,
                      Instant createdAt,
                      Instant updatedAt,
                      String username,
                      String email,
                      BinaryContent profile,
                      Boolean online) {

    public static UserDto of(User user) {
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile(),
                user.isOnline()
        );
    }

    public static List<UserDto> from(List<User> users) {
        return users.stream()
                .map(UserDto::of)
                .toList();
    }
}
