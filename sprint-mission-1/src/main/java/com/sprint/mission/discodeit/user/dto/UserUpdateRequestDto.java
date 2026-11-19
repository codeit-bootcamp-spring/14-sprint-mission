package com.sprint.mission.discodeit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

// 모든 필드는 선택값이다. null이면 변경하지 않고, 값이 있으면 공백일 수 없다
// (빈 문자열이 오면 User.update가 그대로 덮어써 비밀번호가 ""의 해시로 바뀐다)
public record UserUpdateRequestDto(
    @Pattern(regexp = ".*\\S.*", message = "공백일 수 없습니다")
    String newUsername,
    @Pattern(regexp = ".*\\S.*", message = "공백일 수 없습니다")
    String newPassword,
    @Email
    @Pattern(regexp = ".*\\S.*", message = "공백일 수 없습니다")
    String newEmail) {

}
