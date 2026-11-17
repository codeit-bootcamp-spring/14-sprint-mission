package com.sprint.mission.discodeit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDto(
        @NotBlank(message = "username은 필수입니다.")
        @Size(max = 50, message = "username은 50자 이하여야 합니다.")
        String username,

        @NotBlank(message = "email은 필수입니다.")
        @Email(message = "올바른 email 형식이 아닙니다.")
        @Size(max = 100, message = "email은 100자 이하여야 합니다.")
        String email,

        @NotBlank(message = "password는 필수입니다.")
        @Size(max = 60, message = "password는 60자 이하여야 합니다.")
        String password){

}
