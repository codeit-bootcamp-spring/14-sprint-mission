package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BasicEntity{
    private final UUID userId;
    private final Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt){
        super();
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }
    public boolean isOnline() {
        return this.lastActiveAt.isAfter(Instant.now().minus(Duration.ofMinutes(5)));
    }
}
