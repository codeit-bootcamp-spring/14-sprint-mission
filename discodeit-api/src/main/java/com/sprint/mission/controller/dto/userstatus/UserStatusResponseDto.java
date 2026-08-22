package com.sprint.mission.controller.dto.userstatus;

import com.sprint.mission.domain.UserStatus;
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
public class UserStatusResponseDto {
    UUID id;
    Instant createdAt;
    Instant updatedAt;
    UUID userId;
    Instant lastActiveAt;
    boolean online;

    public static UserStatusResponseDto from(UserStatus userStatus) {
        return new UserStatusResponseDto(
                userStatus.getId(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt(),
                userStatus.getUserId(),
                userStatus.getLastActiveAt(),
                userStatus.isOnline()
        );
    }
}
