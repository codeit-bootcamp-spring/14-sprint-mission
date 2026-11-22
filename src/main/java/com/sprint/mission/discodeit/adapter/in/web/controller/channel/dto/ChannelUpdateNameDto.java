package com.sprint.mission.discodeit.adapter.in.web.controller.channel.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ChannelUpdateNameDto(@NotEmpty String newName,
                                   @NotNull String newDescription) {
}
