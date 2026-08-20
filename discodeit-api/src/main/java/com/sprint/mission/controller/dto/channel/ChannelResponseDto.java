package com.sprint.mission.controller.dto.channel;

import com.sprint.mission.domain.Channel;
import com.sprint.mission.domain.ChannelType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChannelResponseDto {
    UUID id;
    String name;
    String description;
    ChannelType channelType;
    Instant updatedAt;

    Instant mostRecentMessageAt;
    List<UUID> participantUserIds;

    public static ChannelResponseDto from(
            Channel channel,
            Instant mostRecentMessageAt,
            List<UUID> participantUserIds
    ) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getChannelType(),
                channel.getUpdatedAt(),
                mostRecentMessageAt,
                List.copyOf(participantUserIds)
        );
    }
}
