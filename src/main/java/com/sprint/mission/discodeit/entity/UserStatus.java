package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserStatus extends UpdatableEntity {

    private static final long serialVersionUID = 1L;
    private static final long ONLINE_THRESHOLD_MINUTES = 5;

    UUID userId;

    @NonFinal
    Instant lastActiveAt;

    private UserStatus(UUID userId,Instant lastActiveAt) {
        super();
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
        updateTimeStamp();
    }


    public boolean isOnline() {
        return lastActiveAt.isAfter(
            Instant.now().minus(ONLINE_THRESHOLD_MINUTES,
                java.time.temporal.ChronoUnit.MINUTES));
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID userId;
        private Instant lastActiveAt = Instant.now();

        public Builder userId(UUID userId) {
          this.userId = userId;
          return this;
        }

        public Builder lastActiveAt(Instant lastActiveAt) {
            this.lastActiveAt = lastActiveAt;
            return this;
        }

        public UserStatus build() {
            return new UserStatus(userId, lastActiveAt);
        }
    }









}
