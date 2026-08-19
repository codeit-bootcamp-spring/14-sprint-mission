package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.domain.message.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class MessageCreationDto {
    @NotBlank
    String content;
    @NotNull
    UUID userId;
    @NotNull
    UUID channelId;
    List<UUID> attachmentIds;

    public Message toMessage() {
        return new Message(content, userId, channelId, attachmentIds);
    }
}
