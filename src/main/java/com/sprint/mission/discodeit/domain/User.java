package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

     private UUID profileId;    // 프로필 이미지인 BinaryContent의 ID

    private String username;
    private String email;
    private String password;

    // UserRequestDto 통해서 User 객체를 생성할것이기 때문에 private으로
    private User(
            String username,
            String email,
            String password,
            UUID profileId
    ) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.username = username;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }


    // 이 정적 메소드를 통해서 User 객체 생성
    public static User create(
            String username,
            String email,
            String password,
            UUID profileId
    ) {
        return new User(
                username,
                email,
                password,
                profileId
        );
    }


    public void updateAccountDetails(User userUpdates) {
        boolean isUpdated = false;

        // username, email, password, profileId 중 하나만 바뀌어도 updatedAt timestamp가 바뀐다
        if (Objects.nonNull(userUpdates.getUsername()) && !Objects.equals(userUpdates.getUsername(), username)) {
            isUpdated = true;
            this.username = userUpdates.getUsername();
        }
        if (Objects.nonNull(userUpdates.getEmail()) && !Objects.equals(userUpdates.getEmail(), email)) {
            isUpdated = true;
            this.email = userUpdates.getEmail();
        }
        if (Objects.nonNull(userUpdates.getPassword()) && !Objects.equals(userUpdates.getPassword(), password)) {
            isUpdated = true;
            this.password = userUpdates.getPassword();
        }
        // 수정
        if (Objects.nonNull(userUpdates.getProfileId()) && !Objects.equals(userUpdates.getProfileId(), profileId)) {
            isUpdated = true;
            this.profileId = userUpdates.getProfileId();
        }

        if (isUpdated) {
            this.updatedAt = Instant.now();
        }
    }


    public boolean matchesPassword(String password) {
        return Objects.equals(this.password, password);
    }
}
