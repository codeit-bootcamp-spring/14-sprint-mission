package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.ModifiableEntity;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
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

    public Channel updateName(String name) {
        if(channelType.equals(ChannelType.PRIVATE)) {
            throw new CustomException(ExceptionType.NO_ACCESS_TO_CHANNEL);
        }
        this.name = name;
        super.markedAsUpdate();
        return this;
    }
}
