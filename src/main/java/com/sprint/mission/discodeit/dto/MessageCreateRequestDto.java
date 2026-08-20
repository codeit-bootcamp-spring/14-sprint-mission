package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public record MessageCreateRequestDto(
    String text,
    UUID user_id,
    UUID channel_id
) {
    public Message toEntity(List<UUID> attachmentIds) {
        return new Message(this.text, this.user_id, this.channel_id, attachmentIds);
    }


}
