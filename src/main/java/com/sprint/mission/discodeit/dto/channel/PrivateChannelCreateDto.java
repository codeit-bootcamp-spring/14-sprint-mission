package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class PrivateChannelCreateDto {
    @NotBlank
    String title;
    @NotNull
    List<UUID> userIds;

    public Channel toChannel() {
        return new Channel(ChannelType.PRIVATE, title, userIds);
    }
}
