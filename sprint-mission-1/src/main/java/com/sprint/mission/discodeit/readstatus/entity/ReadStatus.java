package com.sprint.mission.discodeit.readstatus.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadStatus implements Serializable {
    // 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
    // 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용

    @Serial
    private static final long serialVersionUID = 1L;
    @NonNull
    UUID channelId;
    @NonNull
    UUID userId;
    final UUID readStatusId = UUID.randomUUID();
    final Instant createdAt = Instant.now();
    @NonNull
    Instant lastReadAt;

    public void updateAt(Instant updatedAt) {
        this.lastReadAt = updatedAt;
    }
}
