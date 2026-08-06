package com.sprint.mission.discodeit.message.entity;


import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID userId;
    private final UUID channelId;
    private final UUID messageId = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    @NonNull
    private String message;
    private Instant updatedAt;
    private List<UUID> binaryContentsId;

    public Message(UUID userId, UUID channelId, String message, List<UUID> binaryContentsId) {
        this.userId = userId;
        this.channelId = channelId;
        this.message = message;
        this.binaryContentsId = binaryContentsId;
    }

    public void updateMessage(String updateMessage) {
        if (updateMessage != null && !updateMessage.equals(this.message)) {
            this.message = updateMessage;
            this.updatedAt = Instant.now();
        }
    }
}
