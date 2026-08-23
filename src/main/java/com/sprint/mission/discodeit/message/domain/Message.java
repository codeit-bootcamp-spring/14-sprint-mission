package com.sprint.mission.discodeit.message.domain;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import lombok.Getter;

import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;
    private UUID channelId;
    private UUID authorId;
    private List<UUID> attachmentIds;

    public Message(List<UUID> attachmentIds, String message, UUID channelId, UUID userId) {
        super();
        if(attachmentIds == null){
            attachmentIds = new ArrayList<>();
        }
        this.attachmentIds = attachmentIds;
        this.message = message;
        this.channelId = channelId;
        this.authorId = userId;
    }

    public void update(String message){
        if(message != null) this.message = message;
        this.updateUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", channelId=" + channelId +
                ", UserId=" + authorId +
                '}';
    }
}
