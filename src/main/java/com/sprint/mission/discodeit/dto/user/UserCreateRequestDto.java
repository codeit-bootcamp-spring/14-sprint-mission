package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;


public record UserCreateRequestDto(String name, String email,String password) {


    public User toEntity(UUID profileId){
        return new User(
            this.name,
            this.email,
            this.password,
            profileId
        );
    }
}
