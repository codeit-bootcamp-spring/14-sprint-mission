package com.sprint.mission.discodeit.dto.userdto;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    // OpenAPI에서 요청을 new로 보내니까 dto의 필드도 new로 변경
    private String newUsername;
    private String newEmail;
    private String newPassword;
    private BinaryContentCreateRequestDto profileImageRequestDto;

    /* 현재 내 Service코드에서는 데이터를 getName으로 받는중
    서비스 코드를 원래 getNewUsername으로 바꿔줘야하지만 변경없이 가려고
    getName()메서드 정의
     */
    public String getName() {
        return newUsername;
    }
}
