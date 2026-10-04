package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(min = 4, max = 20, message = "올바른 이름을 입력해주세요")
    String newUsername,
    @Email(message = "올바른 이메일을 입력해주세요")
    String newEmail,
    String newPassword
) {

}
