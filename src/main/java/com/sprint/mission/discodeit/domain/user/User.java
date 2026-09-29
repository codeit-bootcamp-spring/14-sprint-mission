package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

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
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(unique = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BinaryContent profile;

    public User(String username, String email, String password, @Nullable BinaryContent profile) {
        super();
        validateNotNullAndThenAssign(username, ()-> this.username = username);
        validateNotNullAndThenAssign(email, () -> this.email = email);
        validateNotNullAndThenAssign(password, () -> this.password = password);

        assignIfNotNull(profile, () -> this.profile = profile);
    }

    public User update(String username, String email, String password, @Nullable BinaryContent profile) {
        assignIfNotNull(username, ()-> this.username = username);
        assignIfNotNull(email, () -> this.email = email);
        assignIfNotNull(password, () -> this.password = password);
        assignIfNotNull(profile, () -> this.profile = profile);
        return this;
    }

    private <T> void validateNotNullAndThenAssign(T t, Runnable runnable) {
        if (Objects.isNull(t)) {
            throw new IllegalArgumentException("User를 생성/갱신하려면 name, email, password 반드시 필요");
        }
        runnable.run();
    }

    private <T> void assignIfNotNull(T t, Runnable runnable) {
        if (Objects.nonNull(t)) {
            runnable.run();
        }
    }

}
