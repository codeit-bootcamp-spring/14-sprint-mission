package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;


public record PublicChannelCreateDto(@NotNull String name,
                                     @NotNull String description) {

}
