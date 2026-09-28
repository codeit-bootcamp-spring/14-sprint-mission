package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
    @NotBlank(message = "username을 비워둘 수 없습니다.")
    String username,
    @NotBlank(message = "email을 비워둘 수 없습니다.")
    @Email(message = "email 형식이 올바르지 않습니다.")
    String email,
    @NotBlank(message = "password를 비워둘 수 없습니다.")
    String password
) {
}
