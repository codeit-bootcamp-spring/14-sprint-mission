package com.sprint.mission.discodeit.message.entity;


import com.sprint.mission.discodeit.global.entity.BaseEntity;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Message extends BaseEntity {

    private final UUID userId;
    private final UUID channelId;
    @NonNull
    private String message;
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
            super.markUpdated();
        }
    }
}
