package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class ChannelCreationDto {
    @NotNull
    ChannelType channelType;
    @NotNull
    String name;
    @NotNull
    List<UUID> userIds;

    public Channel toChannel() {
        return new Channel(name, userIds);
    }
}
