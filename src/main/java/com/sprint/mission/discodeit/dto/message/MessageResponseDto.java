package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.domain.message.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MessageResponseDto(@NotNull UUID id,
                                 @NotBlank String content,
                                 @NotNull UUID channelId,
                                 @NotNull UUID authorId) {
    public static MessageResponseDto of(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getUserId()
        );
    }
}
