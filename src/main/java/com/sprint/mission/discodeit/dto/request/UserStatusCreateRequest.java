package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull(message = "유저 아이디를 입력해주세요")
    UUID userId,
    @NotNull(message = "마지막 활동 시간을 입력해주세요")
    Instant lastActiveAt
) {

}
