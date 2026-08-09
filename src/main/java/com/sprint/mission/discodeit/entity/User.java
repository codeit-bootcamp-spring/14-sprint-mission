package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserRequestDto;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public class User extends BaseEntity{

    @Serial
    private static final long serialVersionUID = 1L;

    @Nullable
    private UUID profileId;
    private String userName;
    private String email;
    private String password;
    private UserStatus userStatus;

    public User(UserRequestDto userRequestDto) {
        super();
        this.userName = userRequestDto.userName();
        this.email = userRequestDto.email();
        this.password = userRequestDto.password();

    }

    public void update(String username, String email, String password){
        this.userName = username;
        this.email = email;
        this.password = password;
        this.updateUpdatedAt(Instant.now());
    }

    public void updateProfileId(UUID profileId){
        this.profileId = profileId;
    }

    public boolean checkPassword(String password){
        return Objects.equals(password, this.password);
    }


}
