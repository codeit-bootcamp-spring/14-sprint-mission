package com.sprint.mission.discodeit.core.application.readstatus.port.in;

import com.sprint.mission.discodeit.core.application.readstatus.port.in.dto.response.ReadStatusResult;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Validated
public interface ReadStatusUseCase {
    ReadStatusResult create(@NotNull UUID userId, @NotNull UUID channelId, @NotNull Instant lastReadAt);

    List<ReadStatusResult> getAllReadStatusByUserId(@NotNull UUID userId);

    ReadStatusResult updateReadStatus(@NotNull UUID readStatusId, @NotNull Instant newLastReadAt);
}
