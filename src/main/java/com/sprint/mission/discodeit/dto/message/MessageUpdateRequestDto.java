package com.sprint.mission.discodeit.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageUpdateRequestDto {
    private UUID id;
    private String content;

    public static MessageUpdateRequestDto of(UUID id, String content) {
        if (content == null || content.isEmpty()) {
            throw new RuntimeException("Message의 content가 비어있습니다.");
        }

        return new MessageUpdateRequestDto(id, content);
    }
}
