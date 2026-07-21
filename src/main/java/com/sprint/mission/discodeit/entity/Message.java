package com.sprint.mission.discodeit.entity;

import java.util.UUID;


public class Message {
    private final UUID id;
    private long createdAt;
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

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void update(String name, int count){
        this.name = name;
        this.count = count;
        this.updatedAt = System.currentTimeMillis();

    }

}

