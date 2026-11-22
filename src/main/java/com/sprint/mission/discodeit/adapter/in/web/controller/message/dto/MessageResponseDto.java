package com.sprint.mission.discodeit.adapter.in.web.controller.message.dto;

import com.sprint.mission.discodeit.adapter.in.web.controller.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.adapter.in.web.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.core.application.message.port.in.dto.response.MessageResult;
import jakarta.annotation.Nullable;
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
                                 @Nullable UserDto author,
                                 @NotNull List<BinaryContentResponseDto> attachments) {
    public static MessageResponseDto from(MessageResult result) {
        return new MessageResponseDto(
                result.id(),
                result.createdAt(),
                result.updatedAt(),
                result.content(),
                result.channelId(),
                result.author() == null ? null : UserDto.from(result.author()),
                result.attachments().stream()
                        .map(BinaryContentResponseDto::from)
                        .toList()
        );
    }
}
