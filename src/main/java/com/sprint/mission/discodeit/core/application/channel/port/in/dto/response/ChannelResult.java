package com.sprint.mission.discodeit.core.application.channel.port.in.dto.response;

import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.channel.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResult(UUID id,
                            ChannelType type,
                            String name,
                            String description,
                            List<UserResult> participants,
                            Instant lastMessageAt) {
    public static ChannelResult from(Channel channel, List<UserResult> participants, Instant lastMessageAt) {
        return new ChannelResult(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participants,
                lastMessageAt
        );
    }
}
