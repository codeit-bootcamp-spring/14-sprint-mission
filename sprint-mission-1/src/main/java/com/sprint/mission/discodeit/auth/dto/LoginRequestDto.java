package com.sprint.mission.discodeit.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank
    String name,
    @NotBlank
    String password) {

}
