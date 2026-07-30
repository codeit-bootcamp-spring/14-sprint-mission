package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Message {
    private final UUID id;
    private final Long createAt;
    private Long updateAt;
    private String sendMessage;
    private String receiveMessage;

    public Message(String sendMessage, String receiveMessage){
        this.sendMessage = sendMessage;
        this.receiveMessage = receiveMessage;
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = null;
    }
    public void update(String sendMessage, String receiveMessage){
        this.sendMessage = sendMessage;
        this.receiveMessage = receiveMessage;
        this.updateAt = System.currentTimeMillis();
    }
}
