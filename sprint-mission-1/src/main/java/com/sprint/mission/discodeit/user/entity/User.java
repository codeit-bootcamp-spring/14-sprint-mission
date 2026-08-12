package com.sprint.mission.discodeit.user.entity;

import com.sprint.mission.discodeit.global.entity.BaseEntity;
import jakarta.annotation.Nullable;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User extends BaseEntity {

    private String password;
    @NonNull
    private String userName;
    private String email;
    @Nullable
    private UUID binaryId;

    public User(String userName, String password, String email, UUID binaryId) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.binaryId = binaryId;
    }

    public void updateName(String updateName) {
        this.userName = updateName;
        super.markUpdated();
    }

    public void updateEmail(String email) {
        this.email = email;
        super.markUpdated();
    }

    public void updatePassword(String password) {
        this.password = password;
        super.markUpdated();
    }

    public void updateBinaryId(UUID binaryId) {
        this.binaryId = binaryId;
        super.markUpdated();
    }
}
