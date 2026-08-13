package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;import lombok.Getter;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    // 사용자(필드) 별 마지막으로 확인된 접속 시간(필드)을 표현하는 도메인 모델
    // 사용자의 온라인 상태를 확인하기 위해 활용
    // 참조 필드
    private UUID userId;

    private Instant lastActiveAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.userId = userId;
    }

    public void update(){
        Instant now = Instant.now();
        this.lastActiveAt = now;
        this.updatedAt = now;
    }

    // 온라인 상태 확인 메서드
    public boolean isOnline(){
        if (this.lastActiveAt == null){
            return false;
        }
        Instant now = Instant.now();
        Instant onlineStandard = this.lastActiveAt.plus(Duration.ofMinutes(5));

        return onlineStandard.isAfter(now);
    }
}
