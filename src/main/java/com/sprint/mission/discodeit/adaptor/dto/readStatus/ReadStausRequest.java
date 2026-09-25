package com.sprint.mission.discodeit.adaptor.dto.readStatus;

import java.time.Instant;

public record ReadStausRequest(
    Instant recentReadAt
) {

}
