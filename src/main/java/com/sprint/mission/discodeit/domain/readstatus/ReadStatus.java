package com.sprint.mission.discodeit.domain.readstatus;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.List;

/**
 * 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다.
 * 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
 */
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "read_statuses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "channel_id"})
        }
)
public class ReadStatus extends BaseUpdatableEntity {
    @Column(nullable = false)
    private Instant lastReadAt;
    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Channel channel;

    private ReadStatus(User user, Channel channel, Instant lastReadAt) {
        super();
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public static ReadStatus of(User user, Channel channel) {
        return new ReadStatus(user, channel, null);
    }

    public static List<ReadStatus> of(List<User> users, Channel channel) {
        return users.stream()
                .map(user -> of(user, channel))
                .toList();
    }

    public ReadStatus update(Instant newLastReadAt) {
        this.lastReadAt = newLastReadAt;
        return this;
    }
}
