package com.sprint.mission.discodeit.dto.userdto;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    // 프로필 이미지 업데이트시 사용할 필드
    private BinaryContentCreateRequestDto profileImageRequestDto;
}
