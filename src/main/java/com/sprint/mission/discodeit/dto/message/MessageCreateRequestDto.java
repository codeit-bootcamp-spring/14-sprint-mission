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
        if (content == null || content.isEmpty()) {
            throw new RuntimeException("Message의 content가 비어있습니다.");
        }
        if (senderId == null) {
            throw new RuntimeException("Message의 senderId가 비어있습니다.");
        }
        if (channelId == null) {
            throw new RuntimeException("Message의 channelId가 비어있습니다.");
        }


        return new MessageCreateRequestDto(content,senderId, channelId);
    }
}
