package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.domain.message.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(@NotNull UUID id,
                                 @NotNull Instant createdAt,
                                 @NotNull Instant updatedAt,
                                 @NotBlank String content,
                                 @NotNull UUID channelId,
                                 @NotNull UUID authorId,
                                 @NotNull List<UUID> attachmentIds) {
    public static MessageResponseDto of(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannelId(),
                message.getUserId(),
                message.getAttachmentIds()
        );
    }
}
