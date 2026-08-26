package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "유저ID를 비워둘 수 없습니다.")
    UUID userId,
    @NotNull(message = "채널ID를 비워둘 수 없습니다.")
    UUID channelId
) {

}
