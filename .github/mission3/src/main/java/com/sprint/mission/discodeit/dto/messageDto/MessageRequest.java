package com.sprint.mission.discodeit.dto.messageDto;

import java.util.UUID;

public record MessageRequest(
    UUID uuid,
    String content
) {

}
