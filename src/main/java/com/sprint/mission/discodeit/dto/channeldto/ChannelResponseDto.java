package com.sprint.mission.discodeit.dto.channeldto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChannelResponseDto {
    UUID id;
    String channelName;
    ChannelType channelType;
    List<UUID> memberIds;
    Instant lastMessageAt;
    Instant createdAt;
    Instant updatedAt;

    public static ChannelResponseDto from(Channel channel) {
        return from(channel, null);
    }

    public static ChannelResponseDto from(Channel channel, Instant lastMessageAt) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelType(),
                channel.getChannelType() == ChannelType.PRIVATE ? channel.getMemberIds() : channel.getMemberIds(),
                lastMessageAt,
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }
}
