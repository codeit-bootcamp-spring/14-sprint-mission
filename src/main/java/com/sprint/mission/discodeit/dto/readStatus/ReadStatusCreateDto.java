package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class ReadStatusCreateDto {
    @NotNull
    UUID userId;
    @NotNull
    UUID channelId;

    public ReadStatus toReadStatus() {
        return new ReadStatus(userId, channelId);
    }
}
