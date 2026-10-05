package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequestDto(
    @NotBlank(message = "채널 이름은 필수입니다.")
    @Size(max = 100, message = "채널 이름은 100자 이하여야 합니다.")
    String name,

    @Size(max = 500, message = "채널 설명은 500자 이하여야 합니다.")
    String description
) {
    public Channel toEntity() {
        return new Channel(ChannelType.PUBLIC, this.name, this.description);
    }

}
