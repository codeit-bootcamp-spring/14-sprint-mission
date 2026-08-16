package com.sprint.mission.discodeit.entity;


import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private transient final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private int channelNum;

    public Channel(String name, int channelNum) {
        this.id = UUID.randomUUID();
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
        this.name = name;
        this.channelNum = channelNum;
    }


    public void update(String name, int channelNum) {
        this.name = name;
        this.channelNum = channelNum;
        this.updatedAt = System.currentTimeMillis();
    }
}