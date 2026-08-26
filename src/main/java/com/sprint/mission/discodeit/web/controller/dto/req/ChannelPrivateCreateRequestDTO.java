package com.sprint.mission.discodeit.web.controller.dto.req;

import java.util.List;
import java.util.UUID;

public record ChannelPrivateCreateRequestDTO(List<UUID> participantIds)
{ }
