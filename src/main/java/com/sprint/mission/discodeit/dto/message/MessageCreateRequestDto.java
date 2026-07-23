package com.sprint.mission.discodeit.dto.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageCreateRequestDto {
    private String content;
    private UUID senderId;
    private UUID channelId;

    public static MessageCreateRequestDto of(String content, UUID senderId, UUID channelId) {
        return new MessageCreateRequestDto(content,senderId, channelId);
    }
}
