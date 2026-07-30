package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.ModifiableEntity;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public final class User extends ModifiableEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private final UUID profileId;
    @ToString.Include
    private String name;

    public User(String name) {
        super();
        this.profileId = UUID.randomUUID();
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;

        super.markedAsUpdate();
    }
}
