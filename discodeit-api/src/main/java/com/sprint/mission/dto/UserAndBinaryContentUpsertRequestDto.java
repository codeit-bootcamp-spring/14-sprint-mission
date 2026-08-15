package com.sprint.mission.dto;

import com.sprint.mission.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.dto.user.UserUpsertRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserAndBinaryContentUpsertRequestDto {
    private UserUpsertRequestDto user;
    private BinaryContentCreateRequestDto profile;
}
