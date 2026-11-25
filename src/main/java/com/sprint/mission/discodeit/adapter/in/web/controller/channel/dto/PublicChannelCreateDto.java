package com.sprint.mission.discodeit.adapter.in.web.controller.channel.dto;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateDto(@NotBlank String name,
                                     @NotBlank String description) {

}
