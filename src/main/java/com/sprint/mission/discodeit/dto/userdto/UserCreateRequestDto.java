package com.sprint.mission.discodeit.dto.userdto;

import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
    private String email;
    private String name;

    public User toEntity() {
        return new User(this.email, this.name);
    }
}
