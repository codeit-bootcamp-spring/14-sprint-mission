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
        return new MessageUpdateRequestDto(id, content);
    }
}
