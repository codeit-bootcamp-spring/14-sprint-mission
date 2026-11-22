package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;

public record UserUpdateRequest(
    String newUsername,
    @Email(message = "email 형식이 올바르지 않습니다.")
    String newEmail,
    String newPassword
) {
}
