package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름을 비워둘 수 없습니다.")
    @Size(max = 100, message = "채널 이름은 100자 이하여야 합니다.")
    String name,
    String description
) {
}
