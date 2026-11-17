package com.sprint.mission.discodeit.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

// 바꾸고 싶은 값만 보내는 부분 수정이라 @NotBlank 없이 값이 있을 때만 검사
public record UserUpdateRequestDto(
        @Nullable
        @Size(max = 50, message = "username은 50자 이하여야 합니다.")
        String newUsername,

        @Email(message = "올바른 email 형식이 아닙니다.")
        @Size(max = 100, message = "email은 100자 이하여야 합니다.")
        String newEmail,

        @Size(max = 60, message = "password는 60자 이하여야 합니다.")
        String newPassword) {

}
