package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public final class Channel extends Entity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private String name;
    @ToString.Include
    private final List<UUID> usersId;

    public Channel(String name, List<UUID> usersId) {
        super();
        this.name = name;
        this.usersId = new ArrayList<>(usersId);
    }

    public void updateName(String name) {
        this.name = name;

        super.markAsUpdate();
    }

    public boolean containsUser(UUID userId) {
        return usersId.contains(userId);
    }
}
