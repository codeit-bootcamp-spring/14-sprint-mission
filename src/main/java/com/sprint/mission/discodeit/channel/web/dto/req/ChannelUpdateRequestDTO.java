package com.sprint.mission.discodeit.channel.web.dto.req;

import jakarta.validation.constraints.NotBlank;

public record ChannelUpdateRequestDTO(
    @NotBlank(message = "채널 이름은 필수입니다")
    String newName,
    String newDescription
){}
