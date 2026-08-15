package com.sprint.mission.dto.message;

import com.sprint.mission.domain.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageResponseDto {

    UUID id;
    String content;
    UUID senderId;
    UUID channelId;
    List<UUID> attachmentIds;
    Instant updatedAt;

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getContent(),
                message.getSenderId(),
                message.getChannelId(),
                message.getAttachmentIds(),
                message.getUpdatedAt()
        );
    }
}
