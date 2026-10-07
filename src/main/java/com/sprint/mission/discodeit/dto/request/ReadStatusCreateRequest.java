package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "유저 아이디를 입력해주세요")
    UUID userId,
    @NotNull(message = "채널 아이디를 입력해주세요")
    UUID channelId,
    @NotNull(message = "마지막 읽은 시간을 입력해주세요")
    Instant lastReadAt
) {

}
