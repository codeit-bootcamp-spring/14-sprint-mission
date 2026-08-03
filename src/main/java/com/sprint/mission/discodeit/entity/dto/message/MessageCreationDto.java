package com.sprint.mission.discodeit.entity.dto.message;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class MessageCreationDto {
    String content;
    UUID userId;
    UUID channelId;
    List<UUID> attachmentIds;
}
