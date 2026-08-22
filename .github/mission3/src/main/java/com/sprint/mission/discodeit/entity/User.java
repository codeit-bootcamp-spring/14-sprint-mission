package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.util.Instant;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updateAt;
    private String name;
    private String password;
    private String email;

    public User(String name, String password, String email) {
        id = UUID.randomUUID();
        createdAt = Instant.now();
        this.name = name;
        this.password = password;
        this.email = email;
    }


    public User update(String updateName, String password, String email) {
        this.name = updateName;
        this.password = password;
        this.email = email;
        this.updateAt = Instant.now();
        return this;
    }

    @Override
    public String toString() {
        return "이름 = " + this.name + " 메일 주소 = " + this.email;
    }


    public static User create(UserCreateRequest request) {
        return new User(request.name(), request.password(), request.email());
    }
}
