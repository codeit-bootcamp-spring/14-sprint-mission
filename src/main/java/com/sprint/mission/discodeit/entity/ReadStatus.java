package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;import lombok.Getter;

@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    // 추가해야 할 필드 변수를 알기 위해서는 주어진 문장에서 명사를 기준으로 생각해보면 됨.
    // 사용자(필드)가 채널(필드) 별 마지막으로 메시지를 읽은 시간(필드)을 표현하는 도메인 모델
    // 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용

    // 참조 필드
    private UUID userId;
    private UUID channelId;

    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = now;
    }

    public void update(Instant newLastReadAt){
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)){
            this.lastReadAt = newLastReadAt;
            this.updatedAt = Instant.now();
        }
    }
}
