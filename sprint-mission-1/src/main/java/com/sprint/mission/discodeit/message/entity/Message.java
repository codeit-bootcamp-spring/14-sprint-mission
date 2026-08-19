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

    private final UUID authorId;
    private final UUID channelId;
    @NonNull
    private String content;
    private List<UUID> attachmentIds;

    public Message(UUID authorId, UUID channelId, String content, List<UUID> attachmentIds) {
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
        this.attachmentIds = attachmentIds;
    }

    public void updateMessage(String updateMessage) {
        if (updateMessage != null && !updateMessage.equals(this.content)) {
            this.content = updateMessage;
            super.markUpdated();
        }
    }
}
