package com.sprint.mission.discodeit.channel.adapter.api.request;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    List<UUID> participantIds
) {

}
