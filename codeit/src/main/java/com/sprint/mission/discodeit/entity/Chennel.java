package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Chennel {
    private final UUID id;
    private final Long createAt;
    private Long updateAt;
    private String channelName;
    private int channelNumber;

    public Chennel(String channelName, int channelNumber){
        this.channelName = channelName;
        this.channelNumber = channelNumber;
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = null;
    }

    public void update(String channelName, int channelNumber){
        this.channelName = channelName;
        this.channelNumber = channelNumber;
        this.updateAt = System.currentTimeMillis();
    }
}
