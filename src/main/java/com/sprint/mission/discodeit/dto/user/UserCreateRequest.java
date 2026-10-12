package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "username을 비워둘 수 없습니다.")
    @Size(max = 50, message = "username은 50자 이하여야 합니다.")
    String username,
    @NotBlank(message = "email을 비워둘 수 없습니다.")
    @Email(message = "email 형식이 올바르지 않습니다.")
    String email,
    @NotBlank(message = "password를 비워둘 수 없습니다.")
    String password
) {
}
