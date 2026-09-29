package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(@NotNull UUID id,
                                 @NotNull Instant createdAt,
                                 @NotNull Instant updatedAt,
                                 @NotNull User user,
                                 @NotNull Channel channel,
                                 Instant lastReadAt) {
    public static ReadStatusResponse of(ReadStatus created) {
        return new ReadStatusResponse(
                created.getId(),
                created.getCreatedAt(),
                created.getUpdatedAt(),
                created.getUser(),
                created.getChannel(),
                created.getLastReadAt()
        );
    }
}
