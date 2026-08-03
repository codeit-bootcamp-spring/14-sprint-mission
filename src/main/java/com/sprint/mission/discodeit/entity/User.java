package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.common.ModifiableEntity;
import jakarta.annotation.Nullable;
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
    private String name;
    private String email;
    private String password;

    @ToString.Include
    private UUID profileId;

    public User(String name, String email, String password, @Nullable UUID profileId) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void updateName(String name) {
        this.name = name;

        super.markedAsUpdate();
    }
}
