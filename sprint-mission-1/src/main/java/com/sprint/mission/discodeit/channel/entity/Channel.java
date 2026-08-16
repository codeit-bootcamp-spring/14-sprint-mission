package com.sprint.mission.discodeit.channel.entity;

import com.sprint.mission.discodeit.global.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Channel extends BaseEntity {

    private String channelName;
    private ChannelType channelType = ChannelType.PUBLIC;
    private String description;

    public Channel(ChannelType channelType) {
        this.channelType = channelType;
    }

    public Channel(String channelName, ChannelType channelType, String description) {
        this.channelName = channelName;
        this.channelType = channelType;
        this.description = description;
    }


    public void update(String channelName, String description) {
        boolean changed = false;
        if (channelName != null && !channelName.equals(this.channelName)) {
            this.channelName = channelName;
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
