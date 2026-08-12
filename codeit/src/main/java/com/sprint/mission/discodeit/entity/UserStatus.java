package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class UserStatus extends BaseEntity {
    private UUID userId;
    private Instant lastLoginCheckTime;
    private boolean loginCheck;

    protected UserStatus(UUID id, Integer userNum, UUID userId) {
        super(id, userNum);
        this.userId = userId;
    }

    public Instant loginCkeck() {
        this.lastLoginCheckTime = Instant.now();
        return lastLoginCheckTime;
    }

    public boolean lastLoginCheck() {
        Instant nowCheckTime = Instant.now();
        Duration duration = Duration.between(this.lastLoginCheckTime, nowCheckTime);
        long diffSeconds = duration.getSeconds();
        if (300 >= diffSeconds) {
            return loginCheck = true;
        } else {
            return loginCheck = false;
        }
    }

}
