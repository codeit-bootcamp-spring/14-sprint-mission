package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@ToString
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String values;
    private final Channel channel;
    private final User sender;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public Message(String values, Channel channel, User sender) {
        this.values = values;
        this.channel = channel;
        this.sender = sender;

        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}
