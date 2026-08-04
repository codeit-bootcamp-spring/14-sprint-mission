package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class ChannelCreationDto {
    String name;
    List<UUID> usersId;

    public Channel toChannel() {
        return new Channel(name, usersId);
    }
}
