package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

    @Column(nullable = false, unique = true, length = 50)
    String username;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(nullable = false, length = 60)
    String password;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", unique = true)
    BinaryContent profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    UserStatus status;

    private User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static User create(String username, String email, String password) {
        return new User(username, email, password);
    }

    public void update(String username, String email, BinaryContent profile) {
        if (username != null) {
            this.username = username;
        }
        if (email != null) {
            this.email = email;
        }
        if (profile != null) {
            this.profile = profile;
        }
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }
}
