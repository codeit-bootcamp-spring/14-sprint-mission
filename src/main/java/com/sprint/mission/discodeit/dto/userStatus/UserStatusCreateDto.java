package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class UserStatusCreateDto {
    @NotNull
    UUID userId;

    public UserStatus toUserStatus() {
        return new UserStatus(userId);
    }
}
