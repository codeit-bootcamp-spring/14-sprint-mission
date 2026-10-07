package com.sprint.mission.discodeit.common;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant instant,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {}
