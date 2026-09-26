package com.sprint.mission.discodeit.application.readstatus.dto;

import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId
) {

}
