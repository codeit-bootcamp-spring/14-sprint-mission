package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(

    UUID id,
    UserResponseDto author,
    UUID channelId,
    String content,
    List<BinaryContentResponseDto> attachments,
    Instant createdAt,
    Instant updatedAt
) {

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
            message.getId(),
            toAuthorDto(message.getAuthor()),
            message.getChannel().getId(),
            message.getContent(),
            message.getAttachments().stream()
                .map(BinaryContentResponseDto::from)
                .toList(),
            message.getCreatedAt(),
            message.getUpdatedAt()

        );
    }

    private static UserResponseDto toAuthorDto(User author) {
        if (author == null) {
            return null;   // 탈퇴한 유저의 메시지
        }
        boolean online = author.getStatus() != null && author.getStatus().isOnline();
        return UserResponseDto.from(author, online);
    }
}
