package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message extends BaseEntity{

    final UUID senderId;
    final UUID channelId;
    String content;

    private Message(UUID senderId, UUID channelId, String content) {
        super();
        this.senderId = senderId;
        this.channelId = channelId;
        this.content = content;
    }

    public static Message create(UUID senderId, UUID channelId, String content){
        return new Message(senderId, channelId, content);
    }

    public void changeContent(String content){
        this.content = content;
        newUpdatedAt();
    }
}
