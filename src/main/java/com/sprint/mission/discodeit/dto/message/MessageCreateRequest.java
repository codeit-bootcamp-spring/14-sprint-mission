package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    String content,
    @NotNull(message = "channelId를 비워둘 수 없습니다.")
    UUID channelId,
    @NotNull(message = "authorId를 비워둘 수 없습니다.")
    UUID authorId
) {
}
