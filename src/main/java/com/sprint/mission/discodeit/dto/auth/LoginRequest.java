package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "username을 비워둘 수 없습니다.")
    String username,
    @NotBlank(message = "password를 비워둘 수 없습니다.")
    String password
) {
}
