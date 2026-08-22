package com.sprint.mission.discodeit.user.entity;

import com.sprint.mission.discodeit.global.entity.BaseEntity;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private UUID profileId;

    private User(String username, String password, String email, UUID profileId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileId = profileId;
    }

    private User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public static User create(String name, String password, String email, UUID binaryId) {
        return new User(name, password, email, binaryId);
    }

    public static User create(String name, String password, String email) {
        return new User(name, password, email);
    }

    public void update(String name, String password, String email) {
        if (name != null) {
            this.username = name;
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
            this.profileId = binaryId;
            markUpdated();
        }
    }
}
