package com.sprint.mission.discodeit.entity.dto.message;

import lombok.Value;

import java.util.UUID;

@Value
public class MessageCreationDto {
    String content;
    UUID userId;
    UUID channelId;
}
