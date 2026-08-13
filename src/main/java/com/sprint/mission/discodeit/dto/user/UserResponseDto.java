package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserResponseDto {

    UUID id;
    Instant createdAt;
    Instant updatedAt;

    String username;
    String email;
    UUID profileId;
    Boolean online;


    // UserResponseDto를 통해
    public static UserResponseDto from(
            User user,
            UserStatus userStatus
    ) {
        return new UserResponseDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.isOnline()
        );
    }
}
