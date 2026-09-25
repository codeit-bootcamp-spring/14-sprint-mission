package com.sprint.mission.discodeit.adaptor.dto.channelDto;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    List<UUID> participantIds
) {

}
