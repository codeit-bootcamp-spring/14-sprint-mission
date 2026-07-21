package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.UserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@EqualsAndHashCode
@Getter
public class User {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

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
