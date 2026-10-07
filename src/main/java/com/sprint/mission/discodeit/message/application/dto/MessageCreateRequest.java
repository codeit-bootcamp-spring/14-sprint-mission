package com.sprint.mission.discodeit.message.application.dto;

import java.util.UUID;

public record MessageCreateRequest(
    UUID authorId,
    UUID channelId,
    String content
) {

}
