package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadStatus extends UpdatableEntity {

    private static final long serialVersionUID = 1L;

    UUID userId;
    UUID channelId;

    @NonFinal
    Instant lastReadAt;


    private ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void updateLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
        updateTimeStamp();
    }


    public static class Builder {
        private UUID userId;
        private UUID channelId;
        private Instant lastReadAt = Instant.now();

        public Builder userId(UUID userId){
            this.userId = userId;
            return this;
        }

        public Builder channelId(UUID channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder lastReadAt(Instant lastReadAt) {
            this.lastReadAt = lastReadAt;
            return this;
        }

        public ReadStatus build() {
            return new ReadStatus(userId, channelId, lastReadAt);
        }

    }

}
