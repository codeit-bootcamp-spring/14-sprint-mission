package com.sprint.mission.discodeit.dto.channel;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class ChannelCreationDto {
    String title;
    List<UUID> usersId;
}
