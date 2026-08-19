package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.common.ModifiableEntity;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded = true)
@Getter
public final class User extends ModifiableEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private String name;
    private String email;
    private String password;

    @ToString.Include
    private UUID profileId;

    public User(String name, String email, String password, @Nullable UUID profileId) {
        super();
        validateNotNullAndThenAssign(name, ()-> this.name = name);
        validateNotNullAndThenAssign(email, () -> this.email = email);
        validateNotNullAndThenAssign(password, () -> this.password = password);

        assignIfNotNull(profileId, () -> this.profileId = profileId);

        super.markedAsUpdate();
    }

    public void update(String name, String email, String password, @Nullable  UUID profileId) {
        validateNotNullAndThenAssign(name, ()-> this.name = name);
        validateNotNullAndThenAssign(email, () -> this.email = email);
        validateNotNullAndThenAssign(password, () -> this.password = password);

        assignIfNotNull(profileId, () -> this.profileId = profileId);

        super.markedAsUpdate();
    }

    private <T> void validateNotNullAndThenAssign(T t, Runnable runnable) {
        if (Objects.isNull(t)) {
            throw new IllegalArgumentException(String.format("User를 생성/갱신하려면 name, email, password 반드시 필요"));
        }
        runnable.run();
    }

    private <T> void assignIfNotNull(T t, Runnable runnable) {
        if (Objects.nonNull(t)) {
            runnable.run();
        }
    }

}
