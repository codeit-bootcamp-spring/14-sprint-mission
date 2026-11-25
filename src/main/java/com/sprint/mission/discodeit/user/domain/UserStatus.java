package com.sprint.mission.discodeit.user.domain;

import com.sprint.mission.discodeit.common.domain.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @JoinColumn(name = "user_id", unique = true, nullable = false)
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  private User user;
  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  private UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public static UserStatus create(User user, Instant lastActiveAt) {
    return new UserStatus(user, lastActiveAt);
  }

  UserStatus refresh(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
    return this;
  }

  public boolean isOnline() {
    Instant instant = Instant.now().minus(Duration.ofMinutes(5));

    return lastActiveAt.isAfter(instant);
  }
}
