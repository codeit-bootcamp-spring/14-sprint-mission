package com.sprint.mission.discodeit.userstatus.entity;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStatus implements Serializable {
    // 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
    //  사용자의 온라인 상태를 확인하기 위해 활용
    // 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의
    // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
    // updateAt으로 비교? 새로 하나 받기?

    @Serial
    private static final long serialVersionUID = 1L;
    @NotNull
    UUID userId;
    final UUID userStatusId = UUID.randomUUID();
    Instant lastActiveAt;

    public UserStatus(UUID userId) {
        this.userId = userId;
    }

    public void userLogin() {
        this.lastActiveAt = Instant.now();
    }

    public boolean isOnline() {
        return lastActiveAt != null
            && Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5;
    }

    public void updateUserId(UUID userId) {
        this.userId = userId;
    }

    public void updateAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
}
