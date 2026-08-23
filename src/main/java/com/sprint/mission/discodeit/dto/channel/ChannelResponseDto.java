package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
public class ChannelResponseDto {
    @NotNull
    Channel channel;
    @NotNull
    Instant messageLastSentAt;

    List<UUID> userIds;

    public static ChannelResponseDto of(Channel channel, Instant messageLastSentAt, List<UUID> userIds) {
        return new ChannelResponseDto(channel, messageLastSentAt, userIds);
    }
}
