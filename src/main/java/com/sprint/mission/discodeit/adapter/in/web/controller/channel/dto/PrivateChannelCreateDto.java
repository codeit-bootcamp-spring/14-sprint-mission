package com.sprint.mission.discodeit.adapter.in.web.controller.channel.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateDto(@NotEmpty List<UUID> participantIds) {

}
