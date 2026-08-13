package com.sprint.mission.discodeit.service.application.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpsertRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserApplicationService {
    UserResponseDto create(
            UserUpsertRequestDto userCreateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    );
    UserResponseDto findById(UUID userId);
    List<UserResponseDto> findAll();
    UserResponseDto update(
            UUID userId,
            UserUpsertRequestDto userUpdateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    );
    UserResponseDto activateUser(UUID userId);
    void delete(UUID userId);
}
