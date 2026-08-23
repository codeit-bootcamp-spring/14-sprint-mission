package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.ModifiableEntity;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public final class Channel extends ModifiableEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private String name;

    @ToString.Include
    private final ChannelType channelType;

    public Channel(ChannelType channelType, String name) {
        super();
        this.channelType = channelType;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;

        super.markedAsUpdate();
    }
}
