package com.sprint.mission.discodeit.domain.userstatus;

import com.sprint.mission.discodeit.domain.common.ModifiableEntity;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

/**
 * 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
 */
@Getter
@ToString
public class UserStatus extends ModifiableEntity {
    private final UUID userId;
    private Instant lastSeenAt;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        updateLastSeenAt();
    }

    public void updateLastSeenAt() {
        this.lastSeenAt = Instant.now();
        super.markedAsUpdate(lastSeenAt);
    }

    /***
     * 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
     * @return 유저 접속 여부
     */
    public boolean isOnline() {
        Instant threshold = lastSeenAt.plusSeconds(300);
        return Instant.now().isBefore(threshold);
    }

    public UserStatus updateLastActiveAt(Instant newLastActiveAt) {
        this.lastSeenAt = newLastActiveAt;
        markedAsUpdate(newLastActiveAt);
        return this;
    }
}

