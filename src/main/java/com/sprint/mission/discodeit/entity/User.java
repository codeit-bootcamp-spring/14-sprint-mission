package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.UserUpdateRequestDto;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BasicEntity {

    private String name;
    private String email;
    private UUID profileId;


    public User(String name, String email, UUID profileId) {
        super();
        this.name = name;
        this.email = email;
        this.profileId = profileId;
    }



    public void update(String name, String email) {
        if (name != null) {
            this.name = name;
        }
        if (email != null) {
            this.email = email;
        }

    }
    public void updateProfileId(UUID profileId) {
        this.profileId = profileId;
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
