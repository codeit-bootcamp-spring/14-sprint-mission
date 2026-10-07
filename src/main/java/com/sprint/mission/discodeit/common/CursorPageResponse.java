package com.sprint.mission.discodeit.common;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CursorPageResponse<T>(
    List<T> content,
    int size,
    boolean hasNext,
    Instant nextCursor,
    UUID nextIdAfter
) {

}
