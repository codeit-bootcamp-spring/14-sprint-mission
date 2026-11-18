package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")
@ToString(onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {
    @ToString.Include
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false, length = 60)
    private String password;

    @ToString.Include
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @JoinColumn(unique = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent profile;

    @OneToOne(
            mappedBy = "user",
            optional = false,
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private UserStatus status;

    @Column(nullable = false, length = 20)
    private Role role;

    public static User createUser(
            String username,
            String email,
            String password,
            BinaryContent createdProfile
    ) {
        return createAccount(username, email, password, createdProfile, Role.USER);
    }

    public static User createAdmin(
            String username,
            String email,
            String password
    ) {
        return createAccount(username, email, password, null, Role.ADMIN);
    }

    private static User createAccount(String username,
                               String email,
                               String password,
                               BinaryContent createdProfile,
                               Role role) {
        User newUser = new User(username, email, password, createdProfile, role);
        newUser.status = UserStatus.create(newUser);
        return newUser;
    }

    private User(String username, String email, String password, @Nullable BinaryContent profile, Role role) {
        super();
        if (Objects.isNull(username) || Objects.isNull(email) || Objects.isNull(password)) {
            Map<String, Object> details = new HashMap<>();
            details.put("username", username);
            details.put("email", email);
            details.put("password", password);

            throw new UserException(UserExceptionType.USER_MISSING_UPSERT_ARGUMENT, details);
        }
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        assignIfNotNull(profile, () -> this.profile = profile);
    }

    public User update(String username, String email, String password, BinaryContent profile) {
        assignIfNotNull(username, () -> this.username = username);
        assignIfNotNull(email, () -> this.email = email);
        assignIfNotNull(password, () -> this.password = password);
        assignIfNotNull(profile, () -> this.profile = profile);
        return this;
    }


    private <T> void assignIfNotNull(T t, Runnable runnable) {
        if (Objects.nonNull(t)) {
            runnable.run();
        }
    }

    public boolean isOnline() {
        return status.isOnline();
    }

    public Instant getLastSeenAt() {
        return status.getLastSeenAt();
    }

    public UUID getStatusId() {
        return this.status.getId();
    }

    public User updateLastActiveAt(Instant newLastActiveAt) {
        this.status.updateLastSeenAt(newLastActiveAt);
        return this;
    }

    public User updateRole(Role role) {
        this.role = role;
        return this;
    }
}
