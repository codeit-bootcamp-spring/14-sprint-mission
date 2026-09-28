package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름을 비워둘 수 없습니다.")
    String name,
    String description
) {
}
