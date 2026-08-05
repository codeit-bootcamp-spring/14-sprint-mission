package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BasicEntity {

    private String name;
    private String email;
    private UUID profileId;

    public User(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }






    public void setName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{id=" + id +
            ", username='" + name +
            "', email='" + email +
            "', creatAt='" + createdAt +
            "', updatedAt='" + updatedAt +
            "'}";
    }
}
