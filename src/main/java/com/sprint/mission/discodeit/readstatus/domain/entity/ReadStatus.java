package com.sprint.mission.discodeit.readstatus.domain.entity;

import com.sprint.mission.discodeit.baseentity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;



@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    Channel channel;

    @Column(name = "last_read_at", nullable = false)
    Instant lastReadAt;

    static public ReadStatus init(User user, Channel channel, Instant lastReadAt){
        return ReadStatus.builder()
            .user(user)
            .channel(channel)
            .lastReadAt(lastReadAt)
            .build();
    }

    //채널 입장 / 활동 / 퇴장 시 마다 호출
    public void updateReadTime(Instant updateTime){
        this.lastReadAt = updateTime;
    }
}
