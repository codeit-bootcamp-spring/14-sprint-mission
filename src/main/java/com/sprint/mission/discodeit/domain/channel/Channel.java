package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.common.ModifiableEntity;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;

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
