package com.sprint.mission.discodeit.dto.messagedto;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponseDto {
    UUID id;
    String values;
    UUID channelId;
    UUID senderId;
    List<BinaryContentResponseDto> addedContents;
    Instant createdAt;
    Instant updatedAt;

    public static MessageResponseDto from(Message message, List<BinaryContentResponseDto> addedContents) {
        return new MessageResponseDto(
                message.getId(),
                message.getValues(),
                message.getChannelId(),
                message.getSenderId(),
                addedContents,
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
