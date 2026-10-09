package com.sprint.mission.discodeit.adapter.in.controller.dto.channel;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateDto(@NotNull List<UUID> participantIds) {

}
