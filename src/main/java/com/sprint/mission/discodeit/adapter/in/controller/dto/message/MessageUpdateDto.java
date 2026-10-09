package com.sprint.mission.discodeit.adapter.in.controller.dto.message;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateDto(@NotBlank String newContent) {
}
