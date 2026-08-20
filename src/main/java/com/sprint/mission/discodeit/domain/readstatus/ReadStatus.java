package com.sprint.mission.discodeit.domain.readstatus;

import com.sprint.mission.discodeit.domain.common.ModifiableEntity;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

/**
 * 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다.
 * 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
 */
@Getter
@ToString
public class ReadStatus extends ModifiableEntity {
    private final UUID userId;
    private final UUID channelId;

    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public ReadStatus update(Instant newLastReadAt) {
        this.lastReadAt = newLastReadAt;
        markedAsUpdate();
        return this;
    }
}
