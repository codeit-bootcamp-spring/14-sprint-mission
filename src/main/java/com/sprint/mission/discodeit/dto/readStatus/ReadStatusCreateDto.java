package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.util.UUID;

@Value
public class ReadStatusCreateDto {
    @NotBlank
    UUID userId;
    @NotBlank
    UUID channelId;

    public ReadStatus toReadStatus() {
        return new ReadStatus(userId, channelId);
    }
}
