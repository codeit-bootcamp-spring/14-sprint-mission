package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record PublicChannelCreateRequest(
        @NotBlank(message = "채널 이름은 필수입니다.")
        @Size(max = 100, message = "채널 이름은 100자 이하여야 합니다.")
        String name,

        @Size(max = 500, message = "채널 설명은 500자 이하여야 합니다.")
        String description
) {
    public Channel toEntity() {

        return new Channel(ChannelType.PUBLIC, name, description);
    }
}
