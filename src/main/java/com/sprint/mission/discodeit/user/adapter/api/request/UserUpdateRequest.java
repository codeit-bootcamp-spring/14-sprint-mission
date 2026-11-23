package com.sprint.mission.discodeit.user.adapter.api.request;

import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
        message = "영문, 숫자 , 특수문자를 포함해주세요.")
    String newPassword
) {
  
}
