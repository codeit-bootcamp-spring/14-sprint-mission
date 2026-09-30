package com.sprint.mission.discodeit.channel.web.dto.req;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record ChannelPrivateCreateRequestDTO(
    @NotEmpty(message = "참여자는 한명 이상이여야 합니다")
    List<UUID> participantIds
)
{ }
