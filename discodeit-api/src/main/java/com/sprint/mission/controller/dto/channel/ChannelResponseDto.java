package com.sprint.mission.controller.dto.channel;

import com.sprint.mission.domain.Channel;
import com.sprint.mission.domain.ChannelType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChannelResponseDto {
    UUID id;
    Instant createdAt;
    Instant updatedAt;
    ChannelType type;
    String name;
    String description;

    public static ChannelResponseDto from(Channel channel) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getChannelType(),
                channel.getName(),
                channel.getDescription()
        );
    }
}
