package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public final class User extends Entity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private String name;

    public User(String name) {
        super();
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;

        super.markAsUpdate();
    }
}
