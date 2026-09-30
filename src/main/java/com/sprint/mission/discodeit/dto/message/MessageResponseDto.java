package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.message.Message;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
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
    public static MessageResponseDto of(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannelId(),
                UserDto.from(message.getAuthor()),
                BinaryContentResponseDto.from(message.getAttachments())
        );
    }
}
