package com.sprint.misson.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.ToString;

public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private final List<User> channelUsers = new ArrayList<>();
    private Long updatedAt;
    private ChannelType channelType;
    private String channelName;

    public Channel(ChannelType channelType, String channelName) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.channelType = channelType;
        this.channelName = channelName;
    }

    public UUID getId() {
        return this.id;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public ChannelType getChannelType() {
        return this.channelType;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateChannelType(ChannelType channelType) {
        this.channelType = channelType;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "채널 이름 : " + this.channelName + " 채널 유형 : " + this.channelType;
    }

}
