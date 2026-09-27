package com.sprint.mission.discodeit.application.channel.dto;

import com.sprint.mission.discodeit.application.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UserDto> participants,
    Instant lastMessageAt
) {

}
