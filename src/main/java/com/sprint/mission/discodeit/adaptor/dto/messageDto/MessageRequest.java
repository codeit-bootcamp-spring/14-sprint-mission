package com.sprint.mission.discodeit.adaptor.dto.messageDto;

import java.util.UUID;

public record MessageRequest(
    UUID uuid,
    String content
) {

}
