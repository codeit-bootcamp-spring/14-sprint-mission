package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User extends Common {
    private static final long serialVersionUID = 2L;

    private String username;
    private String email;
    private String password;
    /** 프로필 이미지(BinaryContent)의 id. 없을 수 있다. */
    private UUID profileId;

    public User(String username, String email, String password, UUID profileId) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UUID getProfileId() {
        return profileId;
    }

    /** null인 항목은 바꾸지 않는다. 바뀐 값이 하나도 없으면 updatedAt도 건드리지 않는다. */
    public void update(String newUsername, String newEmail, String newPassword) {
        boolean anyValueUpdated = false;
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
            anyValueUpdated = true;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
            anyValueUpdated = true;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            update();
        }
    }

    public void updateProfileId(UUID newProfileId) {
        this.profileId = newProfileId;
        update();
    }

    public String toString() {
        return String.format("유저이름: %s, 이메일: %s", username, email);
    }

}
