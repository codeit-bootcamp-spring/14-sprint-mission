package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;


public record UserCreateRequestDto(String username, String email,String password) {


    public User toEntity(BinaryContent profileId){
        return new User(
            this.username,
            this.email,
            this.password,
            profileId
        );
    }
}
