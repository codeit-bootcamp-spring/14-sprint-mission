package com.sprint.mission.application.user;

import com.sprint.mission.controller.dto.user.UserUpsertRequestDto;
import com.sprint.mission.controller.dto.user.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserApplicationService {
    UserResponseDto create(
            UserUpsertRequestDto userCreateRequest,
            MultipartFile profileImage
    );
    UserResponseDto findById(UUID userId);
    List<UserResponseDto> findAll();
    UserResponseDto update(
            UUID userId,
            UserUpsertRequestDto userUpdateRequest,
            MultipartFile profileImage
    );
    UserResponseDto activateUser(UUID userId);
    void delete(UUID userId);
}
