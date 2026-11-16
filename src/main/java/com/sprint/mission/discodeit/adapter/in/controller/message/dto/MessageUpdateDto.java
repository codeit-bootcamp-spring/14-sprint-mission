package com.sprint.mission.discodeit.adapter.in.controller.message.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateDto(@NotBlank String newContent) {
}
