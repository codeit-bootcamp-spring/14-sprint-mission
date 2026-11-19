package com.sprint.mission.discodeit.adapter.in.controller.message.dto;

import com.sprint.mission.discodeit.core.domain.message.Message;
import com.sprint.mission.discodeit.adapter.in.controller.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
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
                                 @NotNull UserDto author,
                                 @NotNull List<BinaryContentResponseDto> attachments) {
    public static MessageResponseDto of(Message message, boolean authorOnline) {
        return new MessageResponseDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannelId(),
                UserDto.from(message.getAuthor(), authorOnline),
                BinaryContentResponseDto.from(message.getAttachments())
        );
    }
}
