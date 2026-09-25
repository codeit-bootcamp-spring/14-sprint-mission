package com.sprint.mission.discodeit.adaptor.dto.readStatus;

import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId
) {

}
