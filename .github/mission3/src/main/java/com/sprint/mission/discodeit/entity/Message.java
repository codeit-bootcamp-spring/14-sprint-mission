package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.util.Instant;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private final java.util.UUID id;
    private final Long createdAt;
    private final UUID authorId;
    private final UUID channelId;
    private List<UUID> attachmentsIds;
    private Long updatedAt;
    private String msg;


    private Message(String content, UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.msg = content;
        this.authorId = userId;
        this.channelId = channelId;
    }

    public static Message create(MessageCreateRequest messageCreateRequest) {
        return new Message(messageCreateRequest.Content(),messageCreateRequest.userId(), messageCreateRequest.channelId());
    }



    public Message update(String updateMsg) {
        this.msg = updateMsg;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public String toString() {
        return "내용 : " + this.msg;
    }

}
