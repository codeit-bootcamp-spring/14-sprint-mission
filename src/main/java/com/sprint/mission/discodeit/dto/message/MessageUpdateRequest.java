package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(
    @NotBlank(message = "수정할 내용을 비워둘 수 없습니다.")
    String newContent
) {
}
