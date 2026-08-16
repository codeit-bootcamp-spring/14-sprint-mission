package com.sprint.mission.discodeit.user.entity;

import com.sprint.mission.discodeit.global.entity.BaseEntity;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BaseEntity {

    private String name;
    private String password;
    private String email;
    private UUID binaryId;

    private User(String name, String password, String email, UUID binaryId) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.binaryId = binaryId;
    }

    public static User create(String name, String password, String email, UUID binaryId) {
        return new User(name, password, email, binaryId);
    }

    public void update(String name, String password, String email) {
        if (name != null) {
            this.name = name;
        }
        if (password != null) {
            this.password = password;
        }
        if (email != null) {
            this.email = email;
        }
        super.markUpdated();
    }

    public void updateProfile(UUID binaryId) {
        if (binaryId != null) {
            this.binaryId = binaryId;
            markUpdated();
        }
    }
}
