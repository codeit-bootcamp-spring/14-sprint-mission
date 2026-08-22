package com.sprint.mission.discodeit.dto.channelDto;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreate(
    List<UUID> userIds
) {

}
