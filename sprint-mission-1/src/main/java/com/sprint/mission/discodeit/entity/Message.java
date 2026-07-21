package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Message {
    private final User author;
    private String message;
    private final UUID messageId = UUID.randomUUID();
    private final Long createAt = System.currentTimeMillis();
    private final Long updateAt = System.currentTimeMillis();

    public void updateMessage(String updateMessage){
        this.message = updateMessage;
    }
}
