package com.sprint.mission.discodeit.dto.messagedto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponseDto {
    UUID id;
    String values;
    UUID channelId;
    UUID senderId;
    Instant createdAt;
    Instant updatedAt;

    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getValues(),
                message.getChannelId(),
                message.getSenderId(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
