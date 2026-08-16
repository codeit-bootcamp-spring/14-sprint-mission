package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private transient final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private int count;

    public Message (String name, int count){
        this.id = UUID.randomUUID();
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.name = name;
        this.count = count;

    }



    public void update(String name, int count){
        this.name = name;
        this.count = count;
        this.updatedAt = System.currentTimeMillis();

    }

}