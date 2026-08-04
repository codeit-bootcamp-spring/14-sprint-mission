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

    private final List<UUID> usersId;

    @ToString.Include
    private final ChannelType channelType;

    public Channel(ChannelType channelType, String name, List<UUID> usersId) {
        super();
        this.channelType = channelType;
        this.name = name;
        this.usersId = new ArrayList<>(usersId);
    }

    public Channel(String name, List<UUID> usersId) {
        this(ChannelType.PUBLIC, name, usersId);
    }


    public void updateName(String name) {
        this.name = name;

        super.markedAsUpdate();
    }

    public boolean containsUser(UUID userId) {
        return usersId.contains(userId);
    }
}
