package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.user.UserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = {"id"})
@Getter
public class User implements Serializable {
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

    public void update(UserDto dto) {
        this.updatedAt = now();
        this.name = dto.getName();
    }

    private Long now() {
        return System.currentTimeMillis();
    }

}
