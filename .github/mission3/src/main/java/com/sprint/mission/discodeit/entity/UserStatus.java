package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus {

    private UUID uuid;
    private UUID userUuid;
    private boolean isOnline;
    private Long createdAt;
    private Long activityAt;

    public UserStatus(UUID userUuid, Long activityAt) {
        this.uuid = UUID.randomUUID();
        this.userUuid = userUuid;
        this.createdAt = Instant.now();
        this.activityAt = activityAt;
        this.isOnline = (Instant.now() - this.activityAt) < 500;
    }

    public static UserStatus create(User user) {
        return new UserStatus(user.getId(), user.getCreatedAt());
    }


    public UserStatus refresh() {
        this.activityAt = Instant.now();
        return this;
    }

}
