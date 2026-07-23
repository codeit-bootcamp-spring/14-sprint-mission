package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message extends Entity implements Serializable {
    private String content;
    private UUID senderId;
    private UUID channelId;

    private Message(String content, UUID senderId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public static Message from(MessageCreateRequestDto requestDto) {
        return new Message(
                requestDto.getContent(),
                requestDto.getSenderId(),
                requestDto.getChannelId()
        );
    }

    public void update(String content) {
        if (content != null && !content.equals(this.content)) {
            this.content = content;
            updateTimestamp();
        }
    }
}
