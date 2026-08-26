package com.sprint.mission.discodeit.dto.channel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public record ChannelDto(
    UUID id,
    @JsonProperty("channel_type")
    ChannelType type,
    @JsonProperty("channel_name")
    String name,
    String description,
    List<UUID> participantIds,
    Instant lastMessageAt
) {

}
