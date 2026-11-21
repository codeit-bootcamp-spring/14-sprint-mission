package com.sprint.mission.discodeit.message.web.dto.req;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequestDTO(
    @NotBlank
    String newContent
) {

}
