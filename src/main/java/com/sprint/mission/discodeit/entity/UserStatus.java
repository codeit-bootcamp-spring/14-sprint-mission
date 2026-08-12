package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/** 사용자별 마지막 접속 시간. 온라인 여부를 판단하는 데 쓴다. */
public class UserStatus extends Common {
    private static final long serialVersionUID = 1L;

    /** 이 시간 안에 접속 기록이 있으면 접속 중으로 본다. */
    private static final Duration ONLINE_THRESHOLD = Duration.ofMinutes(5);

    private final UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        super();
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    /**
     * 마지막 접속이 5분 이내면 접속 중으로 본다.
     * 판단 기준은 도메인 규칙이라 여기 둔다. 서비스마다 계산하면 규칙이 갈라진다.
     */
    public boolean isOnline() {
        if (lastActiveAt == null) {
            return false;
        }
        return Duration.between(lastActiveAt, Instant.now()).compareTo(ONLINE_THRESHOLD) <= 0;
    }

    public void updateLastActiveAt(Instant newLastActiveAt) {
        this.lastActiveAt = newLastActiveAt;
        update();
    }

}
