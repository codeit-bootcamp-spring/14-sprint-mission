package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름을 입력해주세요")
    @Size(max = 50, message = "채널 이름을 50자 이하로 작성해주세요")
    String name,
    @NotBlank(message = "채널 설명을 입력해주세요")
    String description
) {

}
