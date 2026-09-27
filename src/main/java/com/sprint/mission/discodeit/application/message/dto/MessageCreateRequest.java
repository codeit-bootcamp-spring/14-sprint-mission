package com.sprint.mission.discodeit.application.message.dto;

import java.util.UUID;

public record MessageCreateRequest(
    UUID authorId,
    UUID channelId,
    String content
) {

}
