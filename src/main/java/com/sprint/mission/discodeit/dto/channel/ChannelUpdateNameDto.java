package com.sprint.mission.discodeit.dto.channel;


import jakarta.validation.constraints.NotNull;

public record ChannelUpdateNameDto(@NotNull String newName,
                                   @NotNull String newDescription) {
}
