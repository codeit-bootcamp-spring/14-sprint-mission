package com.sprint.mission.discodeit.adapter.in.web.controller.readStatus.dto;

import com.sprint.mission.discodeit.core.application.readstatus.port.in.dto.response.ReadStatusResult;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(@NotNull UUID id,
                                 @NotNull UUID userId,
                                 @NotNull UUID channelId,
                                 Instant lastReadAt) {
    public static ReadStatusResponse from(ReadStatusResult result) {
        return new ReadStatusResponse(
                result.id(),
                result.userId(),
                result.channelId(),
                result.lastReadAt()
        );
    }
}
