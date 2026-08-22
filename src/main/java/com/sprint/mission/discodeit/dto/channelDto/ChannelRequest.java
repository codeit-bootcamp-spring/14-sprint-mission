package com.sprint.mission.discodeit.dto.channelDto;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record ChannelRequest (
    UUID uuid,
    Long updateAt,
    String channelName,
    String description,
    ChannelType type
){

}
