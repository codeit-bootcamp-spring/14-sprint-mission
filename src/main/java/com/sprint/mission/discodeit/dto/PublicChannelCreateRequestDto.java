package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

public record PublicChannelCreateRequestDto(
    String name,
    String description
) {
    public Channel toEntity() {
        return new Channel(ChannelType.PUBLIC, this.name, this.description);
    }

}
