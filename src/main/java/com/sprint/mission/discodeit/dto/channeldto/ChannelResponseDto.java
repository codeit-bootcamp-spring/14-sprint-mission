package com.sprint.mission.discodeit.dto.channeldto;

import com.sprint.mission.discodeit.entity.Channel;
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
    List<UUID> memberIds;
    Instant createdAt;
    Instant updatedAt;

    public static ChannelResponseDto from(Channel channel) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getMemberIds(),
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }
}
