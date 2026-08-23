package com.sprint.mission.discodeit.dto.userdto;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
    // OpenAPI 규격 맞추기 위해 필요한 필드 추가
    private String email;
    private String username;
    private String password;
    private BinaryContentCreateRequestDto binaryContentCreateRequestDto;

    // 이미지 파일 없이 유저 생성자
    public UserCreateRequestDto(String email, String username, String password) {
        this(email, username, password, null);
    }

    //TODO:나중에 password도 넣어야됌.. 일단은 dto만 맞추기
    public User toEntity() {
        return new User(this.email, this.username);
    }
}
