package com.sprint.mission.discodeit.dto.userdto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
    UUID id;
    String email;
    String name;
    Instant createdAt;
    Instant updatedAt;
    boolean isOnline;

    public static UserResponseDto from(User user, UserStatus userStatus) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                userStatus.isOnline()
        );
    }
    // 게터로 프론트에 필드 쏴주기
    public String getUsername() {
        return name;
    }

    public boolean getOnline() {
        return isOnline;
    }

    public UUID getProfileId() {
        return null;
    }

}
