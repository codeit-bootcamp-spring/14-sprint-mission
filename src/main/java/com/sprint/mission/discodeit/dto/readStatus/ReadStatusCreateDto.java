package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.util.UUID;

@Value
public class ReadStatusCreateDto {
    @NotBlank
    UUID userId;
    @NotBlank
    UUID channelId;
}
