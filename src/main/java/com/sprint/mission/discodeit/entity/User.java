package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus status;

    public User(String username, String email, String password, BinaryContent profile, UserStatus status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.status = status;
    }

    public void update(String newUserName, String newEmail, String newPassword, BinaryContent newProfile) {
        if (newUserName != null&& !newUserName.equals(this.username)) {
            this.username = newUserName;
        }
        if (newEmail != null&& !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null&& !newPassword.equals(this.password)) {
            this.password = newPassword;
        }
        if (newProfile != null) {
            this.profile = newProfile;
        }
    }
    public void updateProfile(BinaryContent profile) {
        this.profile = profile;
    }
    public void updateStatus(UserStatus status) {
        this.status = status;
    }
}
