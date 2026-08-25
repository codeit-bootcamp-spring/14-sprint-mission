package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Getter
public class Channel extends Basic {

    private String channelName; //채널명
    private String description; //채널설명
    private ChannelType type; //공개, 비공개 채널


    private Channel(UUID id, String channelName, String description, ChannelType type) {
        super(id);
        this.channelName = channelName;
        this.description = description;
        this.type = type;
    }

    public void update(String title) {
        if (title != null) {
            this.channelName = title;
        }
        super.updatedAt = Instant.now();
    }

    public static Channel create(UUID id, String channelName, String description, ChannelType type) {
        return new Channel(id, channelName, description, type);
    }
}
