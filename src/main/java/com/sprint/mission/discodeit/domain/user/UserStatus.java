package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/**
 * 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
 */
@Entity
@Table(name = "user_statuses")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {
    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @Column(name = "last_active_at", nullable = false)
    private Instant lastSeenAt;

    public UserStatus(User user) {
        super();
        this.user = user;
        updateLastSeenAt();
    }

    public void updateLastSeenAt() {
        this.lastSeenAt = Instant.now();
    }

    /***
     * 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
     * @return 유저 접속 여부
     */
    public boolean isOnline() {
        Instant threshold = lastSeenAt.plusSeconds(300);
        return Instant.now().isBefore(threshold);
    }
}
