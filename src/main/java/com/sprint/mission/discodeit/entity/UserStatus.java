package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import org.apache.logging.log4j.CloseableThreadContext;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    private UUID userId;
    private Instant lastAccessAt;
    private Instant createdAt;
    private Instant updatedAt;

    // 사용자별 마지막으로 확인된 접속 시간을 표현하는 모델이다. 마지막 접속시간이 현재 시간으로부터 5분 이내면 접속중
    public UserStatus(UUID userId){
        super();
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastAccessAt = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    //시간 업데이트
    public void updateLastAccessAt(){
        this.updatedAt = Instant.now();
        this.lastAccessAt = Instant.now();
    }

    // 온라인 상태를 계산해서 반환, 5분내면
    public boolean isOnline(){
        Instant now = Instant.now();
        return lastAccessAt != null && lastAccessAt.isAfter(now.minusSeconds(300));
    }

}
