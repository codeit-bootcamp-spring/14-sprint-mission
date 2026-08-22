package com.sprint.mission.discodeit.dto.messageDto;

import java.util.UUID;

public record MessageCreateRequest (
    UUID userId,
    UUID channelId,
    String Content
){

}
