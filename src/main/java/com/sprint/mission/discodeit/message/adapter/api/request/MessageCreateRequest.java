package com.sprint.mission.discodeit.message.adapter.api.request;

import java.util.UUID;

public record MessageCreateRequest(
    UUID authorId,
    UUID channelId,
    String content
) {

}
