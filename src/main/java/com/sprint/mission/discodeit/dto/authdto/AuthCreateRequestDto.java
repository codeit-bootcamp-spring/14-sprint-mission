package com.sprint.mission.discodeit.dto.authdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthCreateRequestDto {
    String name;

    /* 서비스 코드, 필드변경 없이 하려고 setter 메서드 추가,
    프론트에서 쏴주는거 매핑해주려고
    */
    public void setUsername(String username) {
        this.name = username;
    }

    public String getUsername() {
        return this.name;
    }
}
