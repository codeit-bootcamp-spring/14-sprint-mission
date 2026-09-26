package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.domain.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelUpdateRequestDto(
        String newName,
        String newDescription
) {
}
