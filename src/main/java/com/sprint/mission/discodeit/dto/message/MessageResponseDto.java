package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.domain.message.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MessageResponseDto(@NotBlank String content,
                                 @NotNull UUID userId,
                                 @NotNull UUID channelId,
                                 List<UUID> attachmentIds) {
    public static MessageResponseDto of(Message message) {
        return new MessageResponseDto(
                message.getContent(),
                message.getUserId(),
                message.getChannelId(),
                message.getAttachmentIds()
        );
    }
}
