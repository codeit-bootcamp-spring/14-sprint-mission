package com.sprint.mission.discodeit.readstatus.domain;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.common.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "read_statuses",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_read_statuses_user_channel",
            columnNames = {"user_id", "channel_id"})
    })
public class ReadStatus extends BaseUpdatableEntity {

  @JoinColumn(name = "channel_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Channel channel;
  @JoinColumn(name = "user_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User user;
  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;

  private ReadStatus(Channel channel, User user, Instant lastReadAt) {
    this.channel = channel;
    this.user = user;
    this.lastReadAt = lastReadAt;
  }

  public ReadStatus update(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
    return this;
  }

  public static ReadStatus create(Channel channel, User user, Instant lastReadAt) {
    return new ReadStatus(channel, user, lastReadAt);
  }
}
