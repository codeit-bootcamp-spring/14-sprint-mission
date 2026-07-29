package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{

    private String message;
    private UUID channelId;
    private UUID UserId;

    public Message(String message, UUID channelId, UUID UserId) {
        super();
        this.message = message;
        this.channelId = channelId;
        this.UserId = UserId;
    }

    public String getMessage(){
        return message;
    }
    public UUID getMemo(){
        return channelId;
    }
    public UUID getMemoText(){
        return UserId;
    }

    public void update(String message){
        this.message = message;
        this.updateUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", channelId=" + channelId +
                ", UserId=" + UserId +
                '}';
    }
}
