package com.sprint.mission.discodeit.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequestDto(
    @NotBlank
    String username,
    @NotBlank
    String password,
    @NotBlank
    String email) {

}
