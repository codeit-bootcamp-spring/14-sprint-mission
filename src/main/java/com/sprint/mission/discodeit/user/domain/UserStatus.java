package com.sprint.mission.discodeit.user.domain;

import com.sprint.mission.discodeit.common.entity.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "user_statuses")
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "last_active_at", nullable = false)
    private Instant lastAccessAt;

    // 사용자별 마지막으로 확인된 접속 시간을 표현하는 모델이다. 마지막 접속시간이 현재 시간으로부터 5분 이내면 접속중
    private UserStatus(User user){
        super();
        this.user = user;
        this.lastAccessAt = Instant.now();
    }

    //시간 업데이트
    public void updateLastAccessAt(){
        this.lastAccessAt = Instant.now();
    }

    // 온라인 상태를 계산해서 반환, 5분내면
    public boolean isOnline(){
        Instant now = Instant.now();
        return lastAccessAt != null && lastAccessAt.isAfter(now.minusSeconds(300));
    }

    public static UserStatus create(User user) {
        return new UserStatus(user);
    }

}
