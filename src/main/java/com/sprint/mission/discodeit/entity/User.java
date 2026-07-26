package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = {"id"})
@Getter
public class User implements Entity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    @ToString.Include
    private String name;

    public User(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = now();
        this.updatedAt = createdAt;

        this.name = name;
    }

    public void updateName(String name) {
        this.updatedAt = now();
        this.name = name;
    }

    private Long now() {
        return System.currentTimeMillis();
    }

}
