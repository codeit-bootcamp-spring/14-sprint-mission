package com.sprint.mission.discodeit.dto.channelDto;

import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record ChannelRequest(
    UUID uuid,
    Long updateAt,
    String channelName,
    String description,
    ChannelType type
) {

}
