package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
    @NotBlank(message = "닉네임을 비워둘 수 없습니다.")
    String nickName,  //<- 데이터 선언부
    @NotBlank(message = "비밀번호를 비워둘 수 없습니다.")
    String password
) {

}