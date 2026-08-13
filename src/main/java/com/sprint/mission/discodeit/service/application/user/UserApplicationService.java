package com.sprint.mission.discodeit.service.application.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserApplicationService {
    UserResponseDto create(
            UserCreateRequestDto userCreateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    );
    UserResponseDto findById(UUID userId);
    List<UserResponseDto> findAll();
    UserResponseDto update(
            UUID userId,
            UserUpdateRequestDto userUpdateRequest,
            BinaryContentCreateRequestDto profileImageRequest
    );
    void delete(UUID userId);
}
