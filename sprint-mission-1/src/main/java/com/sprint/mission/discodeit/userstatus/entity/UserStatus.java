package com.sprint.mission.discodeit.userstatus.entity;

import com.sprint.mission.discodeit.global.entity.BaseEntity;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStatus extends BaseEntity {

    @NotNull
    UUID userId;

    public UserStatus(UUID userId) {
        this.userId = userId;
    }

    public void userLogin() {
        super.markUpdated();
    }

    public boolean isOnline() {
        return super.getUpdatedAt() != null
            && Duration.between(super.getUpdatedAt(), Instant.now()).toMinutes() < 5;
    }

    public void updateUserId(UUID userId) {
        this.userId = userId;
    }

    public void updateAt(Instant lastActiveAt) {
        super.updateAt(lastActiveAt);
    }
}
