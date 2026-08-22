package com.sprint.mission.discodeit.channel.entity;

import com.sprint.mission.discodeit.global.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Channel extends BaseEntity {

    private String name;
    private ChannelType type = ChannelType.PUBLIC;
    private String description;

    public Channel(ChannelType type) {
        this.type = type;
    }

    public Channel(String name, ChannelType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }


    public void update(String channelName, String description) {
        boolean changed = false;
        if (channelName != null && !channelName.equals(this.name)) {
            this.name = channelName;
            changed = true;
        }
        if (description != null && !description.equals(this.description)) {
            this.description = description;
            changed = true;
        }
        if (changed) {
            super.markUpdated();
        }
    }
}
