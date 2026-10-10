package com.sprint.mission.discodeit.message.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

// content는 첨부파일만 보내는 메시지가 있을 수 있어 필수로 두지 않음
public record MessageCreateRequestDto(
        List<UUID> attachmentIds,
        String content,

        @NotNull(message = "channelId는 필수입니다.")
        UUID channelId,

        @NotNull(message = "authorId는 필수입니다.")
        UUID authorId
) {


}
