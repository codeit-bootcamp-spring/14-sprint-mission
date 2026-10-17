package com.sprint.mission.discodeit.readstatus.web.dto.req;

import java.time.Instant;

public record ReadStatusUpdateRequestDTO(
    Instant newLastReadAt
) {}
