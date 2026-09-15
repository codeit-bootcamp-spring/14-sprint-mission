package com.sprint.mission.discodeit.readStatus.domain;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.common.entity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "read_statuses")
@NoArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadTime;

    // 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 모델
    public ReadStatus(User user, Channel channel) {
        super();
        this.user = user;
        this.channel = channel;
        this.lastReadTime = Instant.now();
    }

    // 마지막 읽은 메세지 시간 업뎃
    public void updateTime(Instant time){
        this.lastReadTime = time;
        updateUpdatedAt(Instant.now());
    }

    public static ReadStatus create(User user, Channel channel){
        return new ReadStatus(user, channel);
    }




}
