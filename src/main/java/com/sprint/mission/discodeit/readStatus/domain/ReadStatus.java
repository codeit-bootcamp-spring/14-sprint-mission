package com.sprint.mission.discodeit.readStatus.domain;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import lombok.Getter;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID userId;
    private UUID channelId;
    private Instant lastReadTime;

    // 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 모델
    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
    }

    // 마지막 읽은 메세지 시간 업뎃
    public void updateTime(Instant time){
        this.lastReadTime = time;
        updateUpdatedAt(Instant.now());
    }




}
