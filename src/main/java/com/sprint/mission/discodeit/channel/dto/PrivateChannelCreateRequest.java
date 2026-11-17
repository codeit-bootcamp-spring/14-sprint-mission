package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        @NotEmpty(message = "참여자는 최소 1명 이상이어야 합니다.")
        List<UUID> participantIds
) {
    public Channel toEntity() {

        return new Channel(ChannelType.PRIVATE, null, null);
    }
}
