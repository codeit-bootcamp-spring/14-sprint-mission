package com.sprint.mission.discodeit.dto.userstatusdto;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStatusResponseDto {
    UUID userId;
    Instant lastActiveAt; //login으로 하고싶은데 나중에 login엔티티랑 헷갈릴듯

    UUID id;
    Instant createdAt;
    Instant updatedAt;

    public static UserStatusResponseDto from(UserStatus userStatus) {
        return new UserStatusResponseDto(
                userStatus.getUserId(),
                userStatus.getLastActiveAt(),
                userStatus.getId(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt());
    }
}
