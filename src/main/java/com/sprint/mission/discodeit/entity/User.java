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
    private String email;
    private String password;

    public User(String name, String email, String password) {
        super();
        this.profileId = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name) {
        this(name, null, null);
    }

    public void updateName(String name) {
        this.name = name;

        super.markedAsUpdate();
    }
}
