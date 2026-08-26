package com.sprint.mission.discodeit.web.controller.dto.req;

import java.time.Instant;

public record ReadStatusUpdateRequestDTO(
    Instant newLastReadAt
) {}
