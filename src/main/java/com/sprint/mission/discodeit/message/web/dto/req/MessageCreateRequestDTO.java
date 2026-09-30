package com.sprint.mission.discodeit.message.web.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
public record MessageCreateRequestDTO(
    @NotNull
    UUID authorId,

    @NotNull
    UUID channelId,

    @NotBlank
    String content
) {

}
