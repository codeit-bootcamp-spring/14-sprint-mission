package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record MessageCreateRequest(

    @NotBlank(message = "내용을 비워둘 수 없습니다.")
    String contents,
    UUID channelId,
    UUID authorId

) {

}
