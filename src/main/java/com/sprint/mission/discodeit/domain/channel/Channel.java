package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.domain.common.ModifiableEntity;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public final class Channel extends ModifiableEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private String name;
    @ToString.Include
    private String description;

    @ToString.Include
    private final ChannelType channelType;

    private Channel(ChannelType channelType, String name, String description) {
        super();
        this.channelType = channelType;
        this.name = name;
        this.description = description;
    }

    public static Channel createPublicChannel(String name, String description) {
        return new Channel(ChannelType.PUBLIC, name, description);
    }

    public static Channel createPrivateChannel(List<UUID> usersId) {
        String name = usersId.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(", "));
        String description = null;
        return new Channel(ChannelType.PRIVATE, name, description);
    }

    public Channel updateNameAndDescription(String name, String description) {
        if(channelType.equals(ChannelType.PRIVATE)) {
            throw new CustomException(ExceptionType.PRIVATE_CHANNEL_CANNOT_BE_MODIFIED);
        }
        this.name = name;
        this.description = description;
        super.markedAsUpdate();
        return this;
    }
}
