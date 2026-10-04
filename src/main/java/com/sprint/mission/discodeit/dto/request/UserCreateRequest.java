package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "유저 이름을 입력해주세요")
    @Size(min = 4, max = 20, message = "올바른 이름을 입력해주세요")
    String username,
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일을 입력해주세요")
    String email,
    @NotBlank(message = "비밀번호를 입력해주세요")
    String password
) {

}
