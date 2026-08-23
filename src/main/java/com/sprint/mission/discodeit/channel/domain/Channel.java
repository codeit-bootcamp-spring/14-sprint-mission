package com.sprint.mission.discodeit.channel.domain;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import lombok.Getter;

import java.io.Serial;
import java.time.Instant;

@Getter
public class Channel extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private ChannelType channelType;
    private String name;
    private String description;

    public Channel(ChannelType channelType, String name, String description) {
        super();
        this.channelType = channelType;
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        this.updateUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Channel{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
