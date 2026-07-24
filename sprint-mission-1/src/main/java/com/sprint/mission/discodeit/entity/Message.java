package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Message implements Serializable {
    private final User author;
    @NonNull
    private String message;
    private final UUID messageId = UUID.randomUUID();
    private final Long createAt = System.currentTimeMillis();
    private Long updateAt;

    public void updateMessage(String updateMessage){
        this.message = updateMessage;
        this.updateAt = System.currentTimeMillis();
    }
}
